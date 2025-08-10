package an.imation.filmsapp.data.mapper

import an.imation.filmsapp.data.model.MovieApiModel
import an.imation.filmsapp.domain.model.MovieDomainModel

class MovieDataMapper {
    fun toDomain(apiModel: MovieApiModel): MovieDomainModel? = runCatching {
        MovieDomainModel(
        id = apiModel.id?.toInt()!!,
        title = apiModel.title!!,
        posterUrl = apiModel.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }!!,
        overview = apiModel.overview!!,
        rating = apiModel.rating?.toDouble()!!
        )
    }.getOrElse {
        null
    }
}