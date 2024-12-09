package ro.upt.ac.tooler.data.database

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.TypeConverters

@TypeConverters(value = [Converters::class])
@Database(entities = [ToolEntity::class, SiteEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun siteDao(): SiteDao
}