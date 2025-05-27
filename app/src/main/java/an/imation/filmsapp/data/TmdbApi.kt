package an.imation.filmsapp.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "67273e5ec3b1327d6c97aa62c01c5cd9",
        @Query("page") page: Int = 1
    ): PopularMoviesResponse
}

data class PopularMoviesResponse(
    @SerializedName("results") val movies: List<MovieApiModel>
)
