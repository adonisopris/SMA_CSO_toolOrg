package ro.upt.ac.tooler.data.database

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sites")
data class SiteEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String
)