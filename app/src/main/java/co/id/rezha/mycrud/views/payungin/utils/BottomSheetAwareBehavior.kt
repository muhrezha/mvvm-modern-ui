package co.id.rezha.mycrud.views.payungin.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class BottomSheetAwareBehavior(
    context: Context? = null,
    attrs: AttributeSet? = null
) : AppBarLayout.ScrollingViewBehavior(context, attrs) {

    private var isScrollEnabled = true

    fun setScrollEnabled(enabled: Boolean) {
        isScrollEnabled = enabled
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return isScrollEnabled && super.onStartNestedScroll(parent, child, directTargetChild, target, axes, type)
    }

    override fun onNestedScroll(
        parent: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (isScrollEnabled) {
            super.onNestedScroll(parent, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
        }
    }
}