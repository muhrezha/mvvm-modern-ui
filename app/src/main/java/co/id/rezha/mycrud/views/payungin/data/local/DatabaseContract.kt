package co.id.rezha.mycrud.views.payungin.data.local

// DatabaseContract.kt
object DatabaseContract {

    // Database info
    const val DATABASE_NAME = "app_database.db"
    const val DATABASE_VERSION = 1
//    const val DATABASE_VERSION = 2 Example

    // Kolom baru di version 2
    const val COL_GENDER = "gender"
    const val COL_FULL_NAME = "full_name"


    // Table User
    const val TABLE_USER = "user_table"
    const val COL_ID = "id"
    const val COL_REFRESH_TOKEN = "refresh_token"
    const val COL_TOKEN_TYPE = "token_type"
    const val COL_ACCESS_TOKEN = "access_token"
    const val COL_EXPIRES_IN = "expires_in"
    const val COL_FCM_TOKEN = "fcm_token"
    const val COL_IS_PAKAR_ACTIVE = "is_pakar_active"
    const val COL_CREATED_AT = "created_at"

    // SQL Create Statement untuk version 1
    const val SQL_CREATE_TABLE_USER = """
        CREATE TABLE $TABLE_USER (
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_REFRESH_TOKEN TEXT NOT NULL,
            $COL_TOKEN_TYPE TEXT,
            $COL_ACCESS_TOKEN TEXT NOT NULL,
            $COL_EXPIRES_IN INTEGER,
            $COL_FCM_TOKEN TEXT,
            $COL_IS_PAKAR_ACTIVE TEXT,
            $COL_CREATED_AT INTEGER DEFAULT (strftime('%s','now'))
        )
    """

    // SQL Create Statement untuk version terbaru
    const val SQL_CREATE_TABLE_USER_V2 = """
        CREATE TABLE $TABLE_USER (
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_REFRESH_TOKEN TEXT NOT NULL,
            $COL_TOKEN_TYPE TEXT,
            $COL_ACCESS_TOKEN TEXT NOT NULL,
            $COL_EXPIRES_IN INTEGER,
            $COL_FCM_TOKEN TEXT,
            $COL_IS_PAKAR_ACTIVE TEXT,
            $COL_GENDER TEXT,
            $COL_FULL_NAME TEXT,
            $COL_CREATED_AT INTEGER DEFAULT (strftime('%s','now'))
        )
    """

    // SQL Alter Statements untuk upgrade
    const val SQL_ALTER_TABLE_ADD_GENDER =
        "ALTER TABLE $TABLE_USER ADD COLUMN $COL_GENDER TEXT"

    const val SQL_ALTER_TABLE_ADD_FULL_NAME =
        "ALTER TABLE $TABLE_USER ADD COLUMN $COL_FULL_NAME TEXT"
}