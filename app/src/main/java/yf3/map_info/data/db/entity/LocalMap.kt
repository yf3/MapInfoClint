package yf3.map_info.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalMap(
    @PrimaryKey val id: Int,
    val name: String,
)
