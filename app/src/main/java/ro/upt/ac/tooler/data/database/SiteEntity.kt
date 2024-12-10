package ro.upt.ac.tooler.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "sites")
data class SiteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val details: String = "",
    @TypeConverters(Converters::class)
    val tools: List<Int>? =null
)