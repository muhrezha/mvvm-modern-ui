package co.id.rezha.mycrud.views.payungin.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class AppDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DatabaseContract.SQL_CREATE_TABLE_USER)
//        db.execSQL(DatabaseContract.SQL_CREATE_TABLE_USER_V2)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        // Handle upgrade bertahap
//        if (oldVersion < 2) {
//            db.execSQL(DatabaseContract.SQL_ALTER_TABLE_ADD_GENDER)
//            db.execSQL(DatabaseContract.SQL_ALTER_TABLE_ADD_FULL_NAME)
//        }

//        if (oldVersion < 3) {
//            db.execSQL(DatabaseContract.SQL_ALTER_TABLE_ADD_RELIGION)
//        }

        // Untuk version selanjutnya, tambah:
        // if (oldVersion < 4) { ... }
    }
}