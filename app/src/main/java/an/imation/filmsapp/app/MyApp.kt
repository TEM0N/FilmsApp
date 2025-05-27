package an.imation.filmsapp.app

import an.imation.filmsapp.data.MovieDataMapper
import an.imation.filmsapp.data.MovieRepositoryImpl
import an.imation.filmsapp.data.TmdbApi
import an.imation.filmsapp.domain.FetchPopularMoviesUseCase
import an.imation.filmsapp.domain.IMovieRepository
import an.imation.filmsapp.presentation.MovieViewModel
import android.app.Application
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
    single<TmdbApi> {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }

    single<IMovieRepository> { MovieRepositoryImpl(get(), MovieDataMapper()) }
    factory<FetchPopularMoviesUseCase> { FetchPopularMoviesUseCase(repository = get<IMovieRepository>()) }
    viewModel<MovieViewModel> {
        MovieViewModel(
            fetchMovies = get<FetchPopularMoviesUseCase>()
        )
    }
}