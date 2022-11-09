package yf3.map_info.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalPoi (
    @PrimaryKey val id: Int,
    val title: String,
    val photoAbsPath: String,
    val longitude: Double,
    val latitude: Double,
    val description: String,
    val poiType: Int,
    val mapID: Int
)