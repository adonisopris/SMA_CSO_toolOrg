package ro.upt.ac.tooler.data.database

import androidx.room.Embedded
import androidx.room.Relation

data class SiteWithTools(
    @Embedded val site: SiteEntity,
    @Relation(
        parentColumn = "id",        // Primary key in SiteEntity
        entityColumn = "siteId"     // Foreign key in ToolEntity
    )
    val tools: List<ToolEntity>
)