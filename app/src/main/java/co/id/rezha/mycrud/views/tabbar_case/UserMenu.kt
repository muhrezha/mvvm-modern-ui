package co.id.rezha.mycrud.views.tabbar_case

// UserMenu.kt
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserMenu(
    val food: String,
    val drink: String
) : Parcelable

