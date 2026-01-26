package co.id.rezha.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.id.rezha.core.data.dao.UserDao
import co.id.rezha.core.data.tables.TableUser
import co.id.rezha.core.utlis.RoomConverters

@Database(
    entities = [
        TableUser::class,
    ],
//    version = 1,
    version = 2,
    exportSchema = false
)

@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}