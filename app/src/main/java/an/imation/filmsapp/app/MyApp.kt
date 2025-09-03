package an.imation.filmsapp.app

import an.imation.filmsapp.OkhttpCache.setOkhttpCache
import an.imation.filmsapp.MyOkHttpClient
import an.imation.filmsapp.data.mapper.MovieDataMapper
import an.imation.filmsapp.data.repositoryimpl.MovieRepositoryImpl
import an.imation.filmsapp.data.ITmdbApi
import an.imation.filmsapp.data.database.AppDatabase
import an.imation.filmsapp.data.mapper.DetailsDataMapper
import an.imation.filmsapp.data.mapper.GenreDataMapper
import an.imation.filmsapp.data.repositoryimpl.FavoritesRepositoryImpl
import an.imation.filmsapp.data.repositoryimpl.GenreRepositoryImpl
import an.imation.filmsapp.data.repositoryimpl.MovieByGenreRepositoryImpl
import an.imation.filmsapp.data.repositoryimpl.MovieDetailsRepositoryImpl
import an.imation.filmsapp.data.repositoryimpl.SettingsRepositoryImpl
import an.imation.filmsapp.data.repositoryimpl.WatchlistRepositoryImpl
import an.imation.filmsapp.domain.repository.IFavoritesRepository
import an.imation.filmsapp.domain.repository.IGenreRepository
import an.imation.filmsapp.domain.repository.IMovieByGenreRepository
import an.imation.filmsapp.domain.repository.IMovieDetailsRepository
import an.imation.filmsapp.domain.usecase.FetchPopularMoviesUseCase
import an.imation.filmsapp.domain.repository.IMovieRepository
import an.imation.filmsapp.domain.repository.ISettingsRepository
import an.imation.filmsapp.domain.repository.IWatchlistRepository
import an.imation.filmsapp.domain.usecase.AddToFavoritesUseCase
import an.imation.filmsapp.domain.usecase.AddToWatchlistUseCase
import an.imation.filmsapp.domain.usecase.FetchGenresUseCase
import an.imation.filmsapp.domain.usecase.FetchMovieDetailsUseCase
import an.imation.filmsapp.domain.usecase.FetchMoviesByGenreUseCase
import an.imation.filmsapp.domain.usecase.GetFavoritesUseCase
import an.imation.filmsapp.domain.usecase.GetWatchlistUseCase
import an.imation.filmsapp.domain.usecase.IsFavoriteUseCase
import an.imation.filmsapp.domain.usecase.IsInWatchlistUseCase
import an.imation.filmsapp.domain.usecase.ObserveSettingsUseCase
import an.imation.filmsapp.domain.usecase.RemoveFromFavoritesUseCase
import an.imation.filmsapp.domain.usecase.RemoveFromWatchlistUseCase
import an.imation.filmsapp.domain.usecase.SetLanguageUseCase
import an.imation.filmsapp.presentation.vm.FavoritesViewModel
import an.imation.filmsapp.presentation.vm.GenresViewModel
import an.imation.filmsapp.presentation.vm.MovieDetailsViewModel
import an.imation.filmsapp.presentation.vm.MovieViewModel
import an.imation.filmsapp.presentation.vm.SettingsViewModel
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}
val appModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            //.client(MyOkHttpClient().get())
            .build()
            .setOkhttpCache(androidApplication())
    }

    single<ITmdbApi> {
        get<Retrofit>().create(ITmdbApi::class.java)
    }
    factory<MovieDataMapper> { MovieDataMapper() }
    single<IMovieRepository> {
        MovieRepositoryImpl(
            api = get<ITmdbApi>(),
            mapper = get<MovieDataMapper>()
        )
    }
    factory<FetchPopularMoviesUseCase> { FetchPopularMoviesUseCase(repository = get<IMovieRepository>()) }
    viewModel<MovieViewModel> {
        MovieViewModel(
            fetchMovies = get<FetchPopularMoviesUseCase>()
        )
    }

    factory<GenreDataMapper> { GenreDataMapper() }
    single<IGenreRepository> {
        GenreRepositoryImpl(
            api = get<ITmdbApi>(),
            mapper = get<GenreDataMapper>()
        )
    }

    factory<FetchGenresUseCase> {
        FetchGenresUseCase(repository = get<IGenreRepository>())
    }

    viewModel<GenresViewModel> {
        GenresViewModel(
            fetchGenres = get<FetchGenresUseCase>(),
            fetchMoviesByGenre = get<FetchMoviesByGenreUseCase>()
        )
    }

    single<IMovieByGenreRepository> {
        MovieByGenreRepositoryImpl(
            api = get<ITmdbApi>(),
            mapper = get<MovieDataMapper>()
        )
    }

    factory<FetchMoviesByGenreUseCase> {
        FetchMoviesByGenreUseCase(repository = get<IMovieByGenreRepository>())
    }
    factory<DetailsDataMapper> { DetailsDataMapper() }

    single<IMovieDetailsRepository> {
        MovieDetailsRepositoryImpl(
            api = get<ITmdbApi>(),
            mapper = get<DetailsDataMapper>()
        )
    }

    factory<FetchMovieDetailsUseCase> {
        FetchMovieDetailsUseCase(repository = get<IMovieDetailsRepository>())
    }

    viewModel<MovieDetailsViewModel> { (movieId: Int) ->
        MovieDetailsViewModel(
            fetchDetails = get<FetchMovieDetailsUseCase>(),
            addToFavorites = get<AddToFavoritesUseCase>(),
            removeFromFavorites = get<RemoveFromFavoritesUseCase>(),
            addToWatchlist = get<AddToWatchlistUseCase>(),
            removeFromWatchlist = get<RemoveFromWatchlistUseCase>(),
            isFavorite = get<IsFavoriteUseCase>(),
            isInWatchlist = get<IsInWatchlistUseCase>(),
            movieId = movieId
        )
    }


    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "movies_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().favoritesDao() }
    single { get<AppDatabase>().watchlistDao() }

    single<IFavoritesRepository> { FavoritesRepositoryImpl(get()) }
    single<IWatchlistRepository> { WatchlistRepositoryImpl(get()) }

// usecases
    factory { AddToFavoritesUseCase(get()) }
    factory { RemoveFromFavoritesUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { IsFavoriteUseCase(get()) }

    factory { AddToWatchlistUseCase(get()) }
    factory { RemoveFromWatchlistUseCase(get()) }
    factory { GetWatchlistUseCase(get()) }
    factory { IsInWatchlistUseCase(get()) }

    viewModel<FavoritesViewModel> {
        FavoritesViewModel(
            getFavorites = get(),
            getWatchlist = get()
        )
    }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile("settings") }
        )
    }

    single<ISettingsRepository> {
        SettingsRepositoryImpl(
            dataStore = get<DataStore<Preferences>>(),
            context = androidContext()
        )
    }

    factory<ObserveSettingsUseCase> {
        ObserveSettingsUseCase(get<ISettingsRepository>())
    }

    /*factory<SetThemeUseCase> {
        SetThemeUseCase(get<ISettingsRepository>())
    }*/

    factory<SetLanguageUseCase> {
        SetLanguageUseCase(get<ISettingsRepository>())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            //setTheme = get<SetThemeUseCase>(),
            setLanguage = get<SetLanguageUseCase>(),
            observeSettings = get<ObserveSettingsUseCase>()
        )
    }
}