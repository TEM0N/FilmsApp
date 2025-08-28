package an.imation.filmsapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(movie: FavoriteEntity)

    @Delete
    suspend fun removeFromFavorites(movie: FavoriteEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE movieId = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}