package ro.upt.ac.tooler.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "tools",
    foreignKeys = [
        ForeignKey(
            entity = SiteEntity::class,
            parentColumns = ["id"],
            childColumns = ["siteId"],
            onDelete = ForeignKey.CASCADE   //SET_NULL
        )
    ],
    indices = [Index(value = ["siteId"])]
)
data class ToolEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val image: String,
    val details: String,
    val siteId: Int? = null,
    val startDate: Date? = null,
    val endDate: Date? = null
)
