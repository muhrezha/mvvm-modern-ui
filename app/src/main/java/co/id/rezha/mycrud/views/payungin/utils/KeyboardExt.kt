package co.id.rezha.mycrud.views.payungin.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

// Extension functions untuk Activity
//fun Activity.hideKeyboard() {
//    KeyboardUtils.hideKeyboard(this)
//}

fun Activity.hideKeyboardAndClearFocus() {
    KeyboardUtils.hideKeyboardAndClearFocus(this)
}

// Extension functions untuk Fragment
//fun Fragment.hideKeyboard() {
//    KeyboardUtils.hideKeyboard(this)
//}

// Extension functions untuk View
//fun View.hideKeyboard() {
//    KeyboardUtils.hideKeyboardFromView(context, this)
//}

// Extension functions untuk EditText
fun EditText.hideKeyboard() {
    KeyboardUtils.hideKeyboardFromEditText(this)
}

fun EditText.showKeyboard() {
    KeyboardUtils.showKeyboard(this)
}

// Extension function untuk Fragment
fun Fragment.hideKeyboard() {
    val activity = this.activity ?: return
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = activity.currentFocus ?: View(activity)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboardAndClearFocus(vararg views: View) {
    hideKeyboard()
    views.forEach { it.clearFocus() }
}

// Extension function untuk Activity
fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val view = currentFocus ?: View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.hideKeyboardAndClearFocus(vararg views: View) {
    hideKeyboard()
    views.forEach { it.clearFocus() }
}

// Extension function untuk View
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.hideKeyboardAndClearFocus() {
    hideKeyboard()
    clearFocus()
}