package an.imation.filmsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class, WatchlistEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao
    abstract fun watchlistDao(): WatchlistDao
}