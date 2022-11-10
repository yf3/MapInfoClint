package yf3.map_info.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = LocalPoiType::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("poi_type_id"),
        onDelete = ForeignKey.SET_DEFAULT),
    ForeignKey(
        entity = LocalMap::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("map_id"),
        onDelete = ForeignKey.SET_DEFAULT)
    ]
)
data class LocalPoi (
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo(name = "photo_path")
    val photoAbsPath: String,
    val longitude: Double,
    val latitude: Double,
    val description: String,
    @ColumnInfo(name = "poi_type_id")
    val poiType: Int,
    @ColumnInfo(name = "map_id")
    val mapID: Int
)