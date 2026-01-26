
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import co.id.rezha.mycrud.R

class ShimmerHelper {

    private val animatedViews = mutableListOf<View>()

    fun startShimmer(containers: List<View>) {
        stopShimmer() // Clear previous animations

        containers.forEach { container ->
            val shimmerView = container.findViewById<View>(R.id.shimmer_gray)
            if (shimmerView != null) {
                startShimmerAnimation(shimmerView)
                animatedViews.add(shimmerView)
            }
        }
    }

    fun startShimmer(container: View) {
        startShimmer(listOf(container))
    }

    fun startShimmerFromParent(parent: ViewGroup) {
        val containers = mutableListOf<View>()

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is CoordinatorLayout) {
                containers.add(child)
            }
        }

        startShimmer(containers)
    }

    private fun startShimmerAnimation(view: View) {
        val animation = AlphaAnimation(0.3f, 1.0f).apply {
            duration = 500
            repeatCount = AlphaAnimation.INFINITE
            repeatMode = AlphaAnimation.REVERSE
        }
        view.startAnimation(animation)
    }

    fun stopShimmer() {
        animatedViews.forEach { view ->
            view.clearAnimation()
        }
        animatedViews.clear()
    }

    fun stopShimmer(containers: List<View>) {
        containers.forEach { container ->
            val shimmerView = container.findViewById<View>(R.id.shimmer_gray)
            shimmerView?.clearAnimation()
            animatedViews.remove(shimmerView)
        }
    }

    fun isShimmering(): Boolean {
        return animatedViews.isNotEmpty()
    }
}