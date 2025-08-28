package an.imation.filmsapp.data.repositoryimpl

import an.imation.filmsapp.data.database.FavoritesDao
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.repository.IFavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import an.imation.filmsapp.data.mapper.toDomain
import an.imation.filmsapp.data.mapper.toFavoriteEntity

class FavoritesRepositoryImpl(
    private val dao: FavoritesDao
) : IFavoritesRepository {
    override fun getAll(): Flow<List<MovieDomainModel>> =
        dao.getAllFavorites().map { it.map { entity -> entity.toDomain() } }

    override suspend fun add(movie: MovieDomainModel) =
        dao.addToFavorites(movie.toFavoriteEntity())

    override suspend fun remove(movie: MovieDomainModel) =
        dao.removeFromFavorites(movie.toFavoriteEntity())

    override fun isFavorite(id: Int): Flow<Boolean> = dao.isFavorite(id)
}