package co.id.rezha.mycrud.views.payungin.utils

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment

/**
 * Extension function untuk setup transparent navigation bar di Activity
 */
fun Window.setupTransparentNavigationBar() {
    // Set navigation bar transparan
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        isNavigationBarContrastEnforced = false
        navigationBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        navigationBarColor = Color.TRANSPARENT
    }

    // Enable edge-to-edge
    WindowCompat.setDecorFitsSystemWindows(this, false)
}

/**
 * Extension function untuk setup transparent navigation bar di Fragment
 */
fun Fragment.setupTransparentNavigationBar() {
    activity?.window?.setupTransparentNavigationBar()
}

/**
 * Extension function untuk set fitsSystemWindows ke View
 */
fun View.setFitsSystemWindowsTransparent() {
    fitsSystemWindows = true
}

/**
 * Extension function lengkap dengan status bar transparan juga
 */
fun Window.setupFullTransparentSystemBars() {
    // Navigation bar transparan
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        isNavigationBarContrastEnforced = false
        navigationBarColor = Color.TRANSPARENT
        isStatusBarContrastEnforced = false
        statusBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        navigationBarColor = Color.TRANSPARENT
        statusBarColor = Color.TRANSPARENT
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        statusBarColor = Color.TRANSPARENT
    }

    // Enable edge-to-edge
    WindowCompat.setDecorFitsSystemWindows(this, false)
}