package ro.upt.ac.tooler.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tools")
data class Tool(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val image: String,
    val available: Boolean = true
)