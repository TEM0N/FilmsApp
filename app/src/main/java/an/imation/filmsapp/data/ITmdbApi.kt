package an.imation.filmsapp.data

import an.imation.filmsapp.data.model.PopularMoviesResponseApiModel
import com.andretietz.retrofit.ResponseCache
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ITmdbApi {
    @GET("movie/popular")
    //@ResponseCache(2, TimeUnit.MINUTES)
    suspend fun getPopularMovies(
        ///@Query("api_key") apiKey: String = "67273e5ec3b1327d6c97aa62c01c5cd9",
        @Query("page") page: Int
    ): PopularMoviesResponseApiModel
}

