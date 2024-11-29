package ro.upt.ac.tooler.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ro.upt.ac.tooler.domain.Tool


@Dao
interface ToolDao {
    @Query("SELECT * FROM tools")
    fun getAll(): List<ToolEntity>

    @Insert
    fun add(tool: ToolEntity)

    @Delete
    fun delete(tool : ToolEntity)

    @Query("SELECT * FROM tools WHERE id = :id")
    fun getToolById(id: Int): ToolEntity?
}