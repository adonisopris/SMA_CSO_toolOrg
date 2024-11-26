package ro.upt.ac.tooler.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ToolDao {
    @Query("SELECT * FROM tools")
    fun getAll(): List<ToolEntity>

    @Insert
    fun add(tool: ToolEntity)

    @Delete
    fun delete(tool: ToolEntity)
}