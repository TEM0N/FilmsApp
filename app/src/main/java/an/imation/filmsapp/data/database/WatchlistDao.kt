package an.imation.filmsapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWatchlist(movie: WatchlistEntity)

    @Delete
    suspend fun removeFromWatchlist(movie: WatchlistEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE movieId = :id)")
    fun isInWatchlist(id: Int): Flow<Boolean>
}