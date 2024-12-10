package ro.upt.ac.tooler.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface SiteDao {
    @Query("SELECT * FROM sites")
    fun getAll(): List<SiteEntity>

    @Insert
    fun add(site: SiteEntity)

    @Delete
    fun delete(site: SiteEntity)

    @Query("SELECT * FROM sites WHERE id = :id")
    fun getSiteById(id: Int): SiteEntity?

    @Query("UPDATE sites SET tools = :tools WHERE id = :id")
    fun updateTools(id: Int, tools: String)

}
