package an.imation.filmsapp.app

import an.imation.filmsapp.OkhttpCache.setOkhttpCache
import an.imation.filmsapp.MyOkHttpClient
import an.imation.filmsapp.data.mapper.MovieDataMapper
import an.imation.filmsapp.data.repositoryimpl.MovieRepositoryImpl
import an.imation.filmsapp.data.ITmdbApi
import an.imation.filmsapp.data.mapper.GenreDataMapper
import an.imation.filmsapp.data.repositoryimpl.GenreRepositoryImpl
import an.imation.filmsapp.domain.repository.IGenreRepository
import an.imation.filmsapp.domain.usecase.FetchPopularMoviesUseCase
import an.imation.filmsapp.domain.repository.IMovieRepository
import an.imation.filmsapp.domain.usecase.FetchGenresUseCase
import an.imation.filmsapp.presentation.vm.GenresViewModel
import an.imation.filmsapp.presentation.vm.MovieViewModel
import android.app.Application
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
    single<IGenreRepository> { GenreRepositoryImpl(api = get(), mapper = get()) }
    factory<FetchGenresUseCase> { FetchGenresUseCase(repository = get()) }
    viewModel<GenresViewModel> { GenresViewModel(fetchGenres = get()) }

}