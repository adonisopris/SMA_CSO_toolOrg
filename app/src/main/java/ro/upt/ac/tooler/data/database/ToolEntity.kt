package ro.upt.ac.tooler.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tools")
data class ToolEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val image: String,
    val available: Boolean = true
)
