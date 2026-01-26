package co.id.rezha.mycrud.views.payungin.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import co.id.rezha.mycrud.R

@SuppressLint("StaticFieldLeak")
object LoadingHelper {

    private var currentLoadingView: View? = null

    fun showLoading(loadingView: View?) {
        loadingView?.let {
            currentLoadingView = it
            it.visibility = View.VISIBLE
//            it.setBackgroundColor(Color.parseColor("#07000000"))
            it.isClickable = true
            it.isFocusable = true
            it.isFocusableInTouchMode = true
        }
    }

    fun hideLoading() {
        currentLoadingView?.let {
            it.visibility = View.GONE
            it.setBackgroundColor(Color.TRANSPARENT)
            it.isClickable = false
            it.isFocusable = false
        }
        currentLoadingView = null
    }

    fun showLoadingInActivity(activity: Activity) {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content)
        showLoadingInView(rootView)
    }

    fun showLoadingInView(parentView: ViewGroup) {
        val loadingView = LayoutInflater.from(parentView.context)
            .inflate(R.layout.loading_progress_layout, parentView, false) as RelativeLayout
        loadingView.isClickable = true
        loadingView.isFocusable = true

        parentView.addView(loadingView)
        showLoading(loadingView)
    }
}