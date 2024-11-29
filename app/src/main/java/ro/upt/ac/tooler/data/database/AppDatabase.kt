package ro.upt.ac.tooler.data.database

import androidx.room.RoomDatabase
import androidx.room.Database

@Database(entities = [ToolEntity::class, SiteEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun siteDao(): SiteDao
}