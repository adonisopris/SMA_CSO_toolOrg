package ro.upt.ac.tooler.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siteTypes")
data class SiteTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
)