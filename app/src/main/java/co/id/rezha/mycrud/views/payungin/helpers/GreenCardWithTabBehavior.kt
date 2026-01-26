package co.id.rezha.mycrud.views.payungin.helpers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import co.id.rezha.mycrud.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

class GreenCardWithTabBehavior(context: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    private var initialY = 0f
    private var isPinned = false

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (initialY == 0f) {
            initialY = dependency.y + dependency.height
        }

        val appBar = dependency as AppBarLayout
        val totalScrollRange = appBar.totalScrollRange
        val currentScroll = -appBar.y

        // Calculate translation based on appbar scroll
        val progress = currentScroll / totalScrollRange
        val translationY = (dependency.height * progress).coerceAtLeast(0f)

        if (progress >= 1f) {
            // Fully collapsed - pin to top
            if (!isPinned) {
                applyPinnedStyle(child)
                isPinned = true
            }
            child.y = 0f
        } else {
            // Still scrolling - follow appbar
            if (isPinned) {
                applyNormalStyle(child)
                isPinned = false
            }
            child.y = initialY - translationY
        }

        return true
    }

    private fun applyPinnedStyle(container: View) {
        val greenCard = container.findViewById<CardView>(R.id.greenCard)
        val tabLayout = container.findViewById<TabLayout>(R.id.tabLayout)

        // Style untuk GreenCard
        greenCard?.let {
            it.setCardBackgroundColor(ContextCompat.getColor(it.context, R.color.white))
            it.radius = 16f
            it.elevation = 0f

            // Ubah warna text dan icon di GreenCard
            val tvScore = it.findViewById<TextView>(R.id.tvScore)
            val tvTime = it.findViewById<TextView>(R.id.tvTime)
            val iconMatch = it.findViewById<ImageView>(R.id.iconMatch)

            tvScore?.setTextColor(ContextCompat.getColor(it.context, R.color.black))
            tvTime?.setTextColor(ContextCompat.getColor(it.context, R.color.grey_600))
            iconMatch?.setColorFilter(ContextCompat.getColor(it.context, R.color.black))
        }

        // Style untuk TabLayout
        tabLayout?.let {
            it.setBackgroundColor(ContextCompat.getColor(it.context, R.color.white))
            it.setTabTextColors(
                ContextCompat.getColor(it.context, R.color.black),
                ContextCompat.getColor(it.context, R.color.black)
            )
            it.setSelectedTabIndicatorColor(ContextCompat.getColor(it.context, R.color.green))
        }
    }

    private fun applyNormalStyle(container: View) {
        val greenCard = container.findViewById<CardView>(R.id.greenCard)
        val tabLayout = container.findViewById<TabLayout>(R.id.tabLayout)

        // Style normal untuk GreenCard
        greenCard?.let {
            it.setCardBackgroundColor(ContextCompat.getColor(it.context, R.color.green))
            it.radius = 0f
            it.elevation = 0f

            // Kembalikan warna text dan icon
            val tvScore = it.findViewById<TextView>(R.id.tvScore)
            val tvTime = it.findViewById<TextView>(R.id.tvTime)
            val iconMatch = it.findViewById<ImageView>(R.id.iconMatch)

            tvScore?.setTextColor(ContextCompat.getColor(it.context, R.color.white))
            tvTime?.setTextColor(ContextCompat.getColor(it.context, R.color.white))
            iconMatch?.clearColorFilter()
        }

        // Style normal untuk TabLayout
        tabLayout?.let {
            it.setBackgroundColor(ContextCompat.getColor(it.context, R.color.green))
            it.setTabTextColors(
                ContextCompat.getColor(it.context, R.color.white),
                ContextCompat.getColor(it.context, R.color.white)
            )
            it.setSelectedTabIndicatorColor(ContextCompat.getColor(it.context, R.color.white))
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}