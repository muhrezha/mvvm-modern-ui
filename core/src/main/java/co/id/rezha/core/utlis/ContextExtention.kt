package co.id.rezha.core.utlis

import android.content.Context
import androidx.room.Room
import co.id.rezha.core.data.db.AppDatabase
import co.id.rezha.core.data.db.migrations.MIGRATION_1_2


// get default AppDatabase
val Context.defaultAppDatabase
    get() = Room.databaseBuilder(this, AppDatabase::class.java, "Dbku")
        .addMigrations(
            MIGRATION_1_2,
        )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()