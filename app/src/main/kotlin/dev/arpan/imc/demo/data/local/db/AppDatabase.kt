package dev.arpan.imc.demo.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.arpan.imc.demo.data.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract val usersDao: UsersDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()

        private const val DATABASE_NAME = "users.db"

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE
                ?: synchronized(LOCK) {
                    INSTANCE
                        ?: buildDatabase(context)
                            .also { INSTANCE = it }
                }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}
