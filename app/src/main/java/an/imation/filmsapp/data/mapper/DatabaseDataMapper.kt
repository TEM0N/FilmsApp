package an.imation.filmsapp.data.mapper

import an.imation.filmsapp.data.database.FavoriteEntity
import an.imation.filmsapp.data.database.WatchlistEntity
import an.imation.filmsapp.domain.model.MovieDetailsDomainModel
import an.imation.filmsapp.domain.model.MovieDomainModel


fun FavoriteEntity.toDomain() = MovieDomainModel(
    id = movieId,
    title = title,
    posterUrl = posterUrl,
    overview = "",
    rating = rating
)

fun MovieDomainModel.toFavoriteEntity() = FavoriteEntity(
    movieId = id,
    title = title,
    posterUrl = posterUrl,
    rating = rating
)

fun WatchlistEntity.toDomain() = MovieDomainModel(
    id = movieId,
    title = title,
    posterUrl = posterUrl,
    overview = "",
    rating = rating
)

fun MovieDomainModel.toWatchlistEntity() = WatchlistEntity(
    movieId = id,
    title = title,
    posterUrl = posterUrl,
    rating = rating
)

fun MovieDetailsDomainModel.toDomain(): MovieDomainModel {
    return MovieDomainModel(
        id = this.id,
        title = this.title,
        posterUrl = this.posterUrl ?: "",
        overview = this.overview,
        rating = this.rating
            .replace(",", ".")
            .filter { it.isDigit() || it == '.' }
            .toDoubleOrNull() ?: 0.0
    )
}


