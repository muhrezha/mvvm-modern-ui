package co.id.rezha.mycrud.views.payungin.data.local

// AuthRepository.kt
import android.content.Context
import co.id.rezha.mycrud.views.payungin.data.local.daos.UserDao
import co.id.rezha.mycrud.views.payungin.data.models.responses.AuthResponse

class LocalRepository(context: Context) {
    private val dbHelper = AppDatabaseHelper(context)
    private val userDao = UserDao(dbHelper)

    fun saveAuthData(authResponse: AuthResponse): Boolean {
        return try {
            val result = userDao.saveUser(authResponse)
            result != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAuthData(): AuthResponse? {
        return userDao.getLatestUser()
    }

    fun getAccessToken(): String? {
        return userDao.getLatestUser()?.accessToken
    }

    fun getRefreshToken(): String? {
        return userDao.getLatestUser()?.refreshToken
    }

    fun updateAccessToken(newAccessToken: String): Boolean {
        return userDao.updateAccessToken(newAccessToken)
    }

    fun clearAuthData() {
        userDao.clearUserData()
    }

    fun isLoggedIn(): Boolean {
        return userDao.hasUserData()
    }
}