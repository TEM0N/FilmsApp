package an.imation.filmsapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieApiModel(
    val id: String?,
    val title: String?,
    @SerializedName("poster_path") val posterPath: String?,
    val overview: String?,
    @SerializedName("vote_average") val rating: String?
)