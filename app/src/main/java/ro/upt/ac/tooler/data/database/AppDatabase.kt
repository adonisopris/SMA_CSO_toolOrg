package ro.upt.ac.tooler.data.database

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters


@Database(entities = [ToolEntity::class, SiteEntity::class, SiteTypeEntity::class], version = 8)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun siteDao(): SiteDao
    abstract fun siteTypeDao(): SiteTypeDao
}