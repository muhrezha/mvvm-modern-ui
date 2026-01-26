package co.id.rezha.mycrud.helpers
import android.app.Activity
import android.os.Build
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

/**
 * Simple extension functions untuk BottomNavigationInsets
 * Tanpa config yang complex
 */

// Extension function untuk View
fun View.setupBottomNavigationInsets() {
    val activity = this.context as? Activity ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
    }

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(
            view.paddingLeft,
            view.paddingTop,
            view.paddingRight,
            systemBars.bottom
        )
        insets
    }
}

// Extension function untuk Activity
fun Activity.setupBottomNavigationInsets(bottomNavView: View) {
    bottomNavView.setupBottomNavigationInsets()
}

// Extension function untuk Fragment
fun Fragment.setupBottomNavigationInsets(bottomNavView: View) {
    activity?.let {
        bottomNavView.setupBottomNavigationInsets()
    }
}

// Versi dengan additional padding
fun View.setupBottomNavigationInsetsWithPadding(additionalPadding: Int = 0) {
    val activity = this.context as? Activity ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
    }

    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.setPadding(
            view.paddingLeft,
            view.paddingTop,
            view.paddingRight,
            systemBars.bottom + additionalPadding
        )
        insets
    }
}

fun Activity.setupBottomNavigationInsetsWithPadding(bottomNavView: View, additionalPadding: Int = 0) {
    bottomNavView.setupBottomNavigationInsetsWithPadding(additionalPadding)
}

fun Fragment.setupBottomNavigationInsetsWithPadding(bottomNavView: View, additionalPadding: Int = 0) {
    activity?.let {
        bottomNavView.setupBottomNavigationInsetsWithPadding(additionalPadding)
    }
}