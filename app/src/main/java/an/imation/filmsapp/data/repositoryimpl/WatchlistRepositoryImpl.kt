package an.imation.filmsapp.data.repositoryimpl

import an.imation.filmsapp.data.database.WatchlistDao
import an.imation.filmsapp.data.mapper.toDomain
import an.imation.filmsapp.data.mapper.toWatchlistEntity
import an.imation.filmsapp.domain.model.MovieDomainModel
import an.imation.filmsapp.domain.repository.IWatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchlistRepositoryImpl(
    private val dao: WatchlistDao
) : IWatchlistRepository {
    override fun getAll(): Flow<List<MovieDomainModel>> =
        dao.getAllWatchlist().map { it.map { entity -> entity.toDomain() } }

    override suspend fun add(movie: MovieDomainModel) =
        dao.addToWatchlist(movie.toWatchlistEntity())

    override suspend fun remove(movie: MovieDomainModel) =
        dao.removeFromWatchlist(movie.toWatchlistEntity())

    override fun isInWatchlist(id: Int): Flow<Boolean> = dao.isInWatchlist(id)
}