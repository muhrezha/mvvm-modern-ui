package co.id.rezha.mycrud.views.AppbarCollapse

import android.view.View
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import co.id.rezha.mycrud.R
import com.google.android.material.appbar.AppBarLayout

class GreenCardBehaviorAppbar : CoordinatorLayout.Behavior<CardView>() {

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
        if (dependency is AppBarLayout) {
            val totalScrollRange = dependency.totalScrollRange
            val verticalOffset = Math.abs(dependency.y).toInt()
            val percentage = verticalOffset.toFloat() / totalScrollRange.toFloat()

            // Update background color based on scroll
            updateBackgroundColor(child, percentage)
        }
        return true
    }

    private fun updateBackgroundColor(cardView: CardView, percentage: Float) {
        val greenColor = ContextCompat.getColor(cardView.context, R.color.green)
        val whiteColor = ContextCompat.getColor(cardView.context, R.color.white)

        if (percentage >= 0.8f) {
            cardView.setCardBackgroundColor(whiteColor)
        } else {
            cardView.setCardBackgroundColor(greenColor)
        }
    }
}