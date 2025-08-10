package an.imation.filmsapp.data.model

import com.google.gson.annotations.SerializedName

data class PopularMoviesResponseApiModel(
    @SerializedName("results") val movies: List<MovieApiModel>,
    @SerializedName("total_pages") val totalPages: Int
)