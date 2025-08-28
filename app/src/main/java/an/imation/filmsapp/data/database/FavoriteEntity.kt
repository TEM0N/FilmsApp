package an.imation.filmsapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val movieId: Int,
    val title: String,
    val posterUrl: String,
    val rating: Double
)