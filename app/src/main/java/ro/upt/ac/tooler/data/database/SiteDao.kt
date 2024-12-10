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

    // Fetch a single site with its tools
    @Transaction
    @Query("SELECT * FROM sites WHERE id = :siteId")
    fun getSiteWithTools(siteId: Int): SiteWithTools

    // Fetch all sites with their tools
    @Transaction
    @Query("SELECT * FROM sites")
    fun getAllSitesWithTools(): List<SiteWithTools>


}
