package an.imation.filmsapp.data

import an.imation.filmsapp.domain.MovieDomainModel

class MovieDataMapper {
    fun toDomain(apiModel: MovieApiModel): MovieDomainModel = MovieDomainModel(
        id = apiModel.id,
        title = apiModel.title,
        posterUrl = apiModel.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" },
        overview = apiModel.overview,
        rating = apiModel.rating
    )
}