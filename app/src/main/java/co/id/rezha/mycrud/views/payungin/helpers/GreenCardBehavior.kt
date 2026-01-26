package co.id.rezha.mycrud.views.payungin.helpers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import co.id.rezha.mycrud.R
import com.google.android.material.appbar.AppBarLayout

class GreenCardBehavior(context: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<CardView>(context, attrs) {

    private var initialY = 0f
    private var isPinned = false

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: CardView,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: CardView,
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
            // Fully collapsed - pin to top dengan corner radius dan background putih
            if (!isPinned) {
                applyPinnedStyle(child)
                isPinned = true
            }
            child.y = 0f
        } else {
            // Still scrolling - follow appbar dengan style normal
            if (isPinned) {
                applyNormalStyle(child)
                isPinned = false
            }
            child.y = initialY - translationY
        }

        return true
    }

    private fun applyPinnedStyle(cardView: CardView) {
        // Ubah background card menjadi putih
        cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.white))
        cardView.radius = 16f
        cardView.elevation = 8f

        // Ubah warna text dan icon
        val contentLayout = cardView.findViewById<LinearLayout>(R.id.greenCardContent)
        val tvScore = cardView.findViewById<TextView>(R.id.tvScore)
        val tvTime = cardView.findViewById<TextView>(R.id.tvTime)
        val iconMatch = cardView.findViewById<ImageView>(R.id.iconMatch)
        val btnWatch = cardView.findViewById<Button>(R.id.btnWatchSmall)

        // Ubah warna text menjadi hitam saat pinned
        tvScore?.setTextColor(ContextCompat.getColor(cardView.context, R.color.black))
        tvTime?.setTextColor(ContextCompat.getColor(cardView.context, R.color.grey_100))

        // Ubah warna icon jika perlu (gunakan tint)
        iconMatch?.setColorFilter(ContextCompat.getColor(cardView.context, R.color.black))

        // Ubah style button jika perlu
        btnWatch?.setTextColor(ContextCompat.getColor(cardView.context, R.color.white))
    }

    private fun applyNormalStyle(cardView: CardView) {
        // Kembalikan ke style normal
        cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.yellow))
        cardView.radius = 0f
        cardView.elevation = 0f

        // Kembalikan warna text dan icon
        val contentLayout = cardView.findViewById<LinearLayout>(R.id.greenCardContent)
        val tvScore = cardView.findViewById<TextView>(R.id.tvScore)
        val tvTime = cardView.findViewById<TextView>(R.id.tvTime)
        val iconMatch = cardView.findViewById<ImageView>(R.id.iconMatch)
        val btnWatch = cardView.findViewById<Button>(R.id.btnWatchSmall)

        tvScore?.setTextColor(ContextCompat.getColor(cardView.context, R.color.white))
        tvTime?.setTextColor(ContextCompat.getColor(cardView.context, R.color.white))
        iconMatch?.clearColorFilter()
        btnWatch?.setTextColor(ContextCompat.getColor(cardView.context, R.color.white))
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: CardView,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}