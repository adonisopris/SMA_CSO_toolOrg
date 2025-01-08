package ro.upt.ac.tooler.data.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import ro.upt.ac.tooler.domain.SiteType
import java.util.concurrent.Executors

object RoomDatabase {

    private var appDatabase: AppDatabase? = null

    fun getDb(context: Context): AppDatabase {
        if (appDatabase == null)
            appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "fleetBase-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration(true)
                .build()
        //appDatabase!!.clearAllTables()
        return appDatabase!!
    }


}