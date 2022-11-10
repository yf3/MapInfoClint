package yf3.map_info.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(foreignKeys = [
    ForeignKey(
        entity = LocalMap::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("map_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class LocalPoiType(
    val id: Int,
    val name: String,
    @ColumnInfo(name = "map_id")
    val mapId: Int,
)
