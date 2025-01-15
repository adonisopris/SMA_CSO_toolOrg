package ro.upt.ac.tooler.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "sites")
data class SiteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val details: String = ""
)