package co.id.rezha.mycrud.views.payungin.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

object KeyboardUtils {

    /**
     * Menyembunyikan keyboard dari Activity
     */
    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Menyembunyikan keyboard dari Fragment
     */
    fun hideKeyboard(fragment: Fragment) {
        val activity = fragment.activity
        activity?.let {
            val view = it.currentFocus
            if (view != null) {
                val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    /**
     * Menyembunyikan keyboard dari View tertentu
     */
    fun hideKeyboardFromView(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Menyembunyikan keyboard dari EditText tertentu
     */
    fun hideKeyboardFromEditText(editText: EditText) {
        val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    /**
     * Menampilkan keyboard dan fokus ke EditText
     */
    fun showKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Menyembunyikan keyboard dan menghilangkan fokus dari semua views
     */
    fun hideKeyboardAndClearFocus(activity: Activity) {
        val view = activity.currentFocus
        view?.clearFocus()
        hideKeyboard(activity)
    }
}