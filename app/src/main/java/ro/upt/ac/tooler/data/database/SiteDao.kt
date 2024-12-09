package ro.upt.ac.tooler.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


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
}
