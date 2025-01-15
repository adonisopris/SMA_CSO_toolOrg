package ro.upt.ac.tooler.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface SiteTypeDao {
    @Query("SELECT * FROM siteTypes")
    fun getAll(): List<SiteTypeEntity>

    @Insert
    fun add(siteType: SiteTypeEntity)

    @Delete
    fun delete(siteType : SiteTypeEntity)

    @Query("DELETE FROM siteTypes")
    fun deleteAll()
}