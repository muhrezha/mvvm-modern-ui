package co.id.rezha.mycrud.views.payungin.data.local.daos

// UserDao.kt
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import co.id.rezha.mycrud.views.payungin.data.local.AppDatabaseHelper
import co.id.rezha.mycrud.views.payungin.data.local.DatabaseContract
import co.id.rezha.mycrud.views.payungin.data.models.responses.AuthResponse

class UserDao(private val dbHelper: AppDatabaseHelper) {

    // Insert atau replace user data (hanya simpan 1 data terbaru)
    fun saveUser(authResponse: AuthResponse): Long {
        val db = dbHelper.writableDatabase

        // Hapus semua data lama
        db.delete(DatabaseContract.TABLE_USER, null, null)

        // Insert data baru
        val values = ContentValues().apply {
            put(DatabaseContract.COL_REFRESH_TOKEN, authResponse.refreshToken)
            put(DatabaseContract.COL_TOKEN_TYPE, authResponse.tokenType)
            put(DatabaseContract.COL_ACCESS_TOKEN, authResponse.accessToken)
            put(DatabaseContract.COL_EXPIRES_IN, authResponse.expiresIn)
            put(DatabaseContract.COL_FCM_TOKEN, authResponse.fcmToken)
            put(DatabaseContract.COL_IS_PAKAR_ACTIVE, authResponse.isPakarActive)
        }

        return db.insert(DatabaseContract.TABLE_USER, null, values)
    }

    // Get latest user data
    fun getLatestUser(): AuthResponse? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.TABLE_USER,
            null, // semua kolom
            null, // where clause
            null, // where args
            null, // group by
            null, // having
            "${DatabaseContract.COL_ID} DESC", // order by
            "1" // limit
        )

        return cursor.use {
            if (it.moveToFirst()) {
                AuthResponse(
                    refreshToken = it.getString(it.getColumnIndexOrThrow(DatabaseContract.COL_REFRESH_TOKEN)),
                    tokenType = it.getString(it.getColumnIndexOrThrow(DatabaseContract.COL_TOKEN_TYPE)),
                    accessToken = it.getString(it.getColumnIndexOrThrow(DatabaseContract.COL_ACCESS_TOKEN)),
                    expiresIn = it.getInt(it.getColumnIndexOrThrow(DatabaseContract.COL_EXPIRES_IN)),
                    fcmToken = it.getString(it.getColumnIndexOrThrow(DatabaseContract.COL_FCM_TOKEN)),
                    isDeletionCanceled = null,
                    isPakarActive = it.getString(it.getColumnIndexOrThrow(DatabaseContract.COL_IS_PAKAR_ACTIVE)),
                    papriRefreshToken = null,
                    pakarRefreshToken = null,
                    id = null,
                    email = null,
                    status = null,
                    authExternalId = null,
                    authExternalType = null,
                    isVerifySignup = null,
                    phoneNumber = null,
                    activationCode = null
                )
            } else {
                null
            }
        }
    }

    // Update access token saja
    fun updateAccessToken(newAccessToken: String): Boolean {
        val latestUser = getLatestUser()
        return if (latestUser != null) {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(DatabaseContract.COL_ACCESS_TOKEN, newAccessToken)
            }

            val rowsAffected = db.update(
                DatabaseContract.TABLE_USER,
                values,
                null, // update semua row (hanya ada 1 data)
                null
            )
            rowsAffected > 0
        } else {
            false
        }
    }

    // Hapus semua data user
    fun clearUserData(): Int {
        val db = dbHelper.writableDatabase
        return db.delete(DatabaseContract.TABLE_USER, null, null)
    }

    // Cek apakah ada data user
    fun hasUserData(): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseContract.TABLE_USER,
            arrayOf(DatabaseContract.COL_ID), // hanya ambil ID
            null,
            null,
            null,
            null,
            null,
            "1"
        )

        return cursor.use {
            it.moveToFirst()
        }
    }
}