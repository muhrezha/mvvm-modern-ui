package co.id.rezha.mycrud.views.payungin.views.dashboard.fragments

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.FragmentHomeBinding
import co.id.rezha.mycrud.databinding.ViewProfileFloatingBarBinding
import co.id.rezha.mycrud.databinding.ViewProfilePinnedBarBinding
import java.net.URL
import java.util.concurrent.Executors

class HomeFragment : Fragment() {

    private val imageUrls = arrayOf(
        "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=400&h=400&fit=crop&crop=face",
    )

    private var isEmployee = true
    private val animationDuration = 200L
    private var isInitialized = false

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var profilePinned: ViewProfilePinnedBarBinding
    private lateinit var profileFloating: ViewProfileFloatingBarBinding

    private lateinit var linearRight: LinearLayout
    private lateinit var linearLeft: LinearLayout
    private val handler = Handler(Looper.getMainLooper())
    private var isAnimationCompleted = false


    companion object {
        private const val DELAY_MILLIS = 200L // 3 detik
        private const val ANIMATION_DURATION = 150L
        private const val SPACING_DP = 10f // Jarak antara kedua linear layout
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeProfileBindings()

        loadImageFromUrl(imageUrl = imageUrls[0])

        binding.layoutProfilePinned.root.visibility = View.VISIBLE
        binding.layoutProfileFloating.root.visibility = View.GONE

        binding.layoutSwitcher.visibility = View.VISIBLE

        setupStateSwitchWithHysteresis()

        linearRight = binding.layoutProfileFloating.linearRight
        linearLeft = binding.layoutProfileFloating.linearLeft
//        startDelayedAnimation()


        profilePinned.rlNotificationPinned.setOnClickListener {
            Log.w(Consts.TAG, "Click rlNotificationPinned")
        }

//        binding.rlTransaction.setOnClickListener {
//            Log.w(Consts.TAG, "Click rl_transaction!")
//        }

        profileFloating.rlNotificationFloating.setOnClickListener {
            Log.w(Consts.TAG, "Click rlNotificationFloating!")
        }

        setupSwitch()
        setupInitialLayout()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }

    private fun startDelayedAnimation() {
        handler.postDelayed({
            animateLinearLeftOnly()
        }, DELAY_MILLIS)
    }

    private fun animateLinearLeftOnly() {
        linearLeft.visibility = View.VISIBLE
        // Konversi 30dp ke pixel
        val spacingPx = SPACING_DP * resources.displayMetrics.density

        // Hitung jarak pergeseran (ke kiri sejauh 30dp + lebar layout)
        val layoutWidth = 37 * resources.displayMetrics.density
        val shiftDistance = - (spacingPx + layoutWidth)

        // Hanya linearLeft yang dianimasi (geser ke kiri)
        linearLeft.animate()
            .translationX(shiftDistance)
            .setDuration(ANIMATION_DURATION)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    // FUNGSI UNTUK MENGEMBALIKAN KE STATE AWAL (HIDE)
    fun resetLinearLeftToHidden() {
        // Hapus semua callback dan animasi yang sedang berjalan
        linearLeft.visibility = View.GONE
        handler.removeCallbacksAndMessages(null)
        linearLeft.animate().cancel()

        // Reset posisi dan sembunyikan
        linearLeft.translationX = 0f
        linearLeft.visibility = View.INVISIBLE
        linearLeft.alpha = 1f

        // Reset status animasi
        isAnimationCompleted = false
    }

    // FUNGSI UNTUK MENJALANKAN ULANG ANIMASI DARI AWAL
    fun restartAnimation() {
        resetLinearLeftToHidden()
        startDelayedAnimation()
    }




    private fun initializeProfileBindings() {
        // Cari view secara manual terlebih dahulu
        val pinnedView = binding.root.findViewById<ViewGroup>(R.id.layoutProfilePinned)
        val floatingView = binding.root.findViewById<ViewGroup>(R.id.layoutProfileFloating)

        if (pinnedView != null && floatingView != null) {
            profilePinned = ViewProfilePinnedBarBinding.bind(pinnedView)
            profileFloating = ViewProfileFloatingBarBinding.bind(floatingView)
        } else {
            throw IllegalStateException("Profile views not found")
        }
    }

    private fun loadImageFromUrl(imageUrl: String) {
        showLoading()
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                // Simulate loading delay for better UX
                Thread.sleep(1000)
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                handler.post {
                    displayImage(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post {
                    showError()
                }
            }
        }
    }

    private fun displayImage(bitmap: Bitmap) {
        profilePinned.progressBar.visibility = View.GONE
        profilePinned.errorIcon.visibility = View.GONE
        profilePinned.circularImgV.visibility = View.VISIBLE
        profilePinned.circularImgV.setImageBitmap(bitmap)
        // Optional: Add fade animation
        profilePinned.circularImgV.alpha = 0f
        profilePinned.circularImgV.animate().alpha(1f).setDuration(300).start()

        profileFloating.progressBar.visibility = View.GONE
        profileFloating.errorIcon.visibility = View.GONE
        profileFloating.circularImgV.visibility = View.VISIBLE
        profileFloating.circularImgV.setImageBitmap(bitmap)
        // Optional: Add fade animation
        profileFloating.circularImgV.alpha = 0f
        profileFloating.circularImgV.animate().alpha(1f).setDuration(300).start()
    }

    private fun showLoading() {
        profilePinned.progressBar.visibility = View.VISIBLE
        profilePinned.errorIcon.visibility = View.GONE
        profilePinned.circularImgV.visibility = View.INVISIBLE

        profileFloating.progressBar.visibility = View.VISIBLE
        profileFloating.errorIcon.visibility = View.GONE
        profileFloating.circularImgV.visibility = View.INVISIBLE
    }

    private fun showError() {
        profilePinned.progressBar.visibility = View.GONE
        profilePinned.errorIcon.visibility = View.VISIBLE
        profilePinned.circularImgV.visibility = View.INVISIBLE

        profileFloating.progressBar.visibility = View.GONE
        profileFloating.errorIcon.visibility = View.VISIBLE
        profileFloating.circularImgV.visibility = View.INVISIBLE
    }

    private fun clearImage() {
        profilePinned.progressBar.visibility = View.GONE
        profilePinned.errorIcon.visibility = View.GONE
        profilePinned.circularImgV.visibility = View.VISIBLE
        profilePinned.circularImgV.setImageDrawable(null)
        profilePinned.circularImgV.setBackgroundResource(R.drawable.circle_image)

        profileFloating.progressBar.visibility = View.GONE
        profileFloating.errorIcon.visibility = View.GONE
        profileFloating.circularImgV.visibility = View.VISIBLE
        profileFloating.circularImgV.setImageDrawable(null)
        profileFloating.circularImgV.setBackgroundResource(R.drawable.circle_image)
    }

    private fun setupSwitch() {
        binding.tvPribadi.setOnClickListener {
            switchToPribadi()
        }
        binding.tvEmployee.setOnClickListener {
            switchToEmployee()
        }
    }

    private fun switchToPribadi() {
        if (!isEmployee) {
            isEmployee = true
            animateSelectionChange()
            updateSelection()
            // Tambahkan logic lainnya ketika switch ke Mobile
            onSwitchChanged(true)
        }
    }

    private fun switchToEmployee() {
        if (isEmployee) {
            isEmployee = false
            animateSelectionChange()
            updateSelection()
            // Tambahkan logic lainnya ketika switch ke Email
            onSwitchChanged(false)
        }
    }

    private fun setupInitialLayout() {
        // Wait for layout to be measured and laid out
        binding.switchContainer.post {
            if (!isInitialized) {
                initializeIndicator()
                isInitialized = true
            }
        }
    }

    private fun updateSelection() {
        binding.tvPribadi.isSelected = isEmployee
        binding.tvEmployee.isSelected = !isEmployee

        val yellowColor = ContextCompat.getColor(requireContext(), R.color.yellow)
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)
        // Update accessibility
        binding.tvPribadi.setTextColor(if (isEmployee) whiteColor else yellowColor)
        binding.tvEmployee.setTextColor(if (!isEmployee) whiteColor else yellowColor)
    }


    private fun animateSelectionChange() {
        if (!isInitialized) return
        val targetPosition = if (isEmployee) 0f else 1f
        val optionWidth = binding.tvPribadi.width.toFloat()
        val targetTranslationX = targetPosition * optionWidth
        // Create smooth hero animation
        val animator = ValueAnimator.ofFloat(
            binding.selectionIndicator.translationX,
            targetTranslationX
        )
        animator.duration = animationDuration
        // Gunakan interpolator untuk animasi yang smooth
        animator.interpolator = android.view.animation.AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            binding.selectionIndicator.translationX = value
            // Optional: Add subtle scale effect for more dynamic animation
            val progress = animation.animatedFraction
            if (progress < 0.5f) {
                val scale = 0.98f + (progress * 0.04f) // 0.98 -> 1.02
                binding.selectionIndicator.scaleX = scale
                binding.selectionIndicator.scaleY = scale
            } else {
                val scale = 1.02f - ((progress - 0.5f) * 0.04f) // 1.02 -> 0.98 -> 1.0
                binding.selectionIndicator.scaleX = scale
                binding.selectionIndicator.scaleY = scale
            }
        }
        // Reset scale after animation
        animator.doOnEnd {
            binding.selectionIndicator.scaleX = 1.0f
            binding.selectionIndicator.scaleY = 1.0f
        }

        animator.start()
    }

    private fun initializeIndicator() {
        val optionWidth = binding.tvPribadi.width
        if (optionWidth > 0) {
            // Set initial position for Mobile (default)
            binding.selectionIndicator.translationX = 0f
            binding.selectionIndicator.isVisible = true
            // Update initial states
            updateSelection()
        }
    }

    /**
     * Callback ketika switch berubah
     * @param isEmployee true jika Pribadi terpilih, false jika Email terpilih
     */
    private fun onSwitchChanged(isPribadi: Boolean) {
        // Tambahkan logic yang ingin dijalankan ketika switch berubah
        // Contoh: update data, refresh UI, dll.
        when (isPribadi) {
            true -> {
                // Logic ketika Pribadi dipilih
                // Contoh: tampilkan data mobile, panggil API, dll.
            }
            false -> {
                // Logic ketika Employee dipilih
                // Contoh: tampilkan data email, panggil API, dll.
            }
        }
    }

    /**
     * Method untuk mendapatkan state current selection dari luar fragment
     */
    fun isEmployeeSelected(): Boolean {
        return isEmployee
    }

    /**
     * Method untuk programmatically switch selection dari luar fragment
     */
    fun setSelection(isPribadi: Boolean) {
        if (isPribadi) {
            switchToPribadi()
        } else {
            switchToEmployee()
        }
    }




/*    private fun setupAppBarStateListener() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.yellow)
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()

            Log.w(Consts.TAG, "Scroll - Offset: $verticalOffset, Percentage: ${"%.2f".format(percentage)}")

            // Kontrol background color
            val interpolatedColor = if (percentage > 0.8f) {
                whiteColor
            } else {
                val smoothPercentage = (percentage / 0.8f).coerceIn(0f, 1f)
                val red = (Color.red(greenColor) * (1 - smoothPercentage) + Color.red(whiteColor) * smoothPercentage).toInt()
                val green = (Color.green(greenColor) * (1 - smoothPercentage) + Color.green(whiteColor) * smoothPercentage).toInt()
                val blue = (Color.blue(greenColor) * (1 - smoothPercentage) + Color.blue(whiteColor) * smoothPercentage).toInt()
                Color.rgb(red, green, blue)
            }
//            binding.yellowCard.setCardBackgroundColor(interpolatedColor)

            // Kontrol visibility berdasarkan state collapse/expand
            if (percentage > 0.95f) {
                // COLLAPSED (lebih dari 95% scrolled)
                binding.layoutProfil.visibility = View.GONE
                binding.layoutSwitcher.visibility = View.VISIBLE
                Log.w(Consts.TAG, "STATE: COLLAPSED - Show layoutSwitcher, Hide layoutProfil")
            } else {
                // EXPANDED atau SEDANG SCROLL
                binding.layoutProfil.visibility = View.VISIBLE
                binding.layoutSwitcher.visibility = View.GONE
                Log.w(Consts.TAG, "STATE: EXPANDED - Show layoutProfil, Hide layoutSwitcher")
            }
        }
    }

    private fun setupAppBarStateWithAnimation() {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.yellow)
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()

            // Background color
            val interpolatedColor = if (percentage > 0.8f) {
                whiteColor
            } else {
                val smoothPercentage = (percentage / 0.8f).coerceIn(0f, 1f)
                val red = (Color.red(greenColor) * (1 - smoothPercentage) + Color.red(whiteColor) * smoothPercentage).toInt()
                val green = (Color.green(greenColor) * (1 - smoothPercentage) + Color.green(whiteColor) * smoothPercentage).toInt()
                val blue = (Color.blue(greenColor) * (1 - smoothPercentage) + Color.blue(whiteColor) * smoothPercentage).toInt()
                Color.rgb(red, green, blue)
            }
//            binding.yellowCard.setCardBackgroundColor(interpolatedColor)

            // Visibility dengan alpha animation
            if (percentage > 0.9f) {
                // Fade out profil, fade in car
                if (binding.layoutProfil.visibility == View.VISIBLE) {
                    binding.layoutProfil.animate().alpha(0f).setDuration(200).withEndAction {
                        binding.layoutProfil.visibility = View.GONE
                    }
                    binding.layoutSwitcher.alpha = 0f
                    binding.layoutSwitcher.visibility = View.VISIBLE
                    binding.layoutSwitcher.animate().alpha(1f).setDuration(200)
                }
            } else {
                // Fade in profil, fade out car
                if (binding.layoutSwitcher.visibility == View.VISIBLE) {
                    binding.layoutSwitcher.animate().alpha(0f).setDuration(200).withEndAction {
                        binding.layoutSwitcher.visibility = View.GONE
                    }
                    binding.layoutProfil.alpha = 0f
                    binding.layoutProfil.visibility = View.VISIBLE
                    binding.layoutProfil.animate().alpha(1f).setDuration(200)
                }
            }
        }
    }

    private fun setupSmoothCrossfade() {
        val yellowColor = ContextCompat.getColor(requireContext(), R.color.yellow)
        val whiteColor = ContextCompat.getColor(requireContext(), R.color.white)

        // Inisialisasi visibility di awal - DIBALIK
        binding.layoutSwitcher.visibility = View.VISIBLE
        binding.layoutSwitcher.alpha = 1f
        binding.layoutProfil.visibility = View.GONE
        binding.layoutProfil.alpha = 0f

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()

            // Background color
//            binding.yellowCard.setCardBackgroundColor(if (percentage > 0.8f) whiteColor else yellowColor)

            // Smooth crossfade DIBALIK antara 0.3 dan 0.7 percentage
            val crossfadeProgress = ((percentage - 0.3f) / 0.4f).coerceIn(0f, 1f)

            when {
                percentage <= 0.3f -> {
                    // Fully show car (AWAL)
                    binding.layoutSwitcher.alpha = 1f
                    binding.layoutSwitcher.visibility = View.VISIBLE
                    binding.layoutProfil.alpha = 0f
                    binding.layoutProfil.visibility = View.GONE
                }
                percentage >= 0.7f -> {
                    // Fully show profil (AKHIR)
                    binding.layoutSwitcher.alpha = 0f
                    binding.layoutSwitcher.visibility = View.GONE
                    binding.layoutProfil.alpha = 1f
                    binding.layoutProfil.visibility = View.VISIBLE
                }
                else -> {
                    // Crossfade transition DIBALIK
                    if (binding.layoutSwitcher.visibility != View.VISIBLE) {
                        binding.layoutSwitcher.visibility = View.VISIBLE
                    }
                    if (binding.layoutProfil.visibility != View.VISIBLE) {
                        binding.layoutProfil.visibility = View.VISIBLE
                    }

                    // Fade out car, fade in profil
                    binding.layoutSwitcher.alpha = 1f - crossfadeProgress
                    binding.layoutProfil.alpha = crossfadeProgress
                }
            }
        }
    }

    private fun setupAppBarScrollWithBackground() {
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()

            // Kontrol visibility dan background
            when {
                percentage < 0.3f -> {
                    // Expanded: show car with red background
                    binding.layoutSwitcher.visibility = View.VISIBLE
                    binding.layoutProfil.visibility = View.GONE
//                    binding.yellowCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                }
                percentage > 0.9f -> {
                    // Collapsed: show profil with yellow background
                    binding.layoutSwitcher.visibility = View.GONE
                    binding.layoutProfil.visibility = View.VISIBLE
//                    binding.yellowCard.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.yellow))
                }
                else -> {
                    // Transisi: crossfade
                    val crossfadeProgress = ((percentage - 0.3f) / 0.4f).coerceIn(0f, 1f)

                    binding.layoutSwitcher.visibility = View.VISIBLE
                    binding.layoutProfil.visibility = View.VISIBLE
                    binding.layoutSwitcher.alpha = 1f - crossfadeProgress
                    binding.layoutProfil.alpha = crossfadeProgress

                    // Interpolasi background color
                    val yellowColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                    val redColor = ContextCompat.getColor(requireContext(), R.color.red)
                    val red = (Color.red(redColor) * (1 - crossfadeProgress) + Color.red(yellowColor) * crossfadeProgress).toInt()
                    val green = (Color.green(redColor) * (1 - crossfadeProgress) + Color.green(yellowColor) * crossfadeProgress).toInt()
                    val blue = (Color.blue(redColor) * (1 - crossfadeProgress) + Color.blue(yellowColor) * crossfadeProgress).toInt()

//                    binding.yellowCard.setCardBackgroundColor(Color.rgb(red, green, blue))
                }
            }
        }
    }

    private fun setupAppBarScrollWithBackground2() {
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()

            // Kontrol visibility dan background
            when {
                percentage < 0.3f -> {
                    binding.layoutSwitcher.visibility = View.VISIBLE
                    binding.layoutProfil.visibility = View.GONE
                    // Force render setelah perubahan visibility
                    binding.layoutSwitcher.post { binding.layoutSwitcher.requestLayout() }
                }
                percentage > 0.9f -> {
                    binding.layoutSwitcher.visibility = View.GONE
                    binding.layoutProfil.visibility = View.VISIBLE
                    binding.layoutProfil.post { binding.layoutProfil.requestLayout() }
                }
                else -> {
                    val crossfadeProgress = ((percentage - 0.3f) / 0.4f).coerceIn(0f, 1f)

                    binding.layoutSwitcher.visibility = View.VISIBLE
                    binding.layoutProfil.visibility = View.VISIBLE
                    binding.layoutSwitcher.alpha = 1f - crossfadeProgress
                    binding.layoutProfil.alpha = crossfadeProgress

                    // Force render keduanya
                    binding.layoutSwitcher.post {
                        binding.layoutSwitcher.requestLayout()
                        binding.layoutProfil.requestLayout()
                    }
                }
            }
        }
    }*/

    private fun setupStateSwitchWithHysteresis() {
        var currentState = "pinned"

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()

            // Hysteresis: threshold berbeda untuk show car vs show profil
            when {
                percentage < 0.55f -> {
                    // Show car (hanya jika < 55%)
                    if (currentState != "pinned") {
                        binding.layoutSwitcher.visibility = View.VISIBLE
                        binding.layoutProfilePinned.root.visibility = View.VISIBLE
                        binding.layoutProfileFloating.root.visibility = View.GONE
                        resetLinearLeftToHidden()
                        currentState = "pinned"
                    }
                }
                percentage > 0.85f -> {
                    // Show profil (hanya jika > 85%)
                    if (currentState != "floating") {
                        binding.layoutSwitcher.visibility = View.GONE
                        binding.layoutProfilePinned.root.visibility = View.GONE
                        binding.layoutProfileFloating.root.visibility = View.VISIBLE
                        startDelayedAnimation()
                        currentState = "floating"
                    }
                }
                // Between 40-60%: maintain current state (mencegah flickering)
            }
        }
    }


}

// Extension function for doOnEnd
fun ValueAnimator.doOnEnd(action: () -> Unit) {
    addListener(object : android.animation.Animator.AnimatorListener {
        override fun onAnimationStart(animation: android.animation.Animator) {}
        override fun onAnimationEnd(animation: android.animation.Animator) { action() }
        override fun onAnimationCancel(animation: android.animation.Animator) {}
        override fun onAnimationRepeat(animation: android.animation.Animator) {}
    })
}