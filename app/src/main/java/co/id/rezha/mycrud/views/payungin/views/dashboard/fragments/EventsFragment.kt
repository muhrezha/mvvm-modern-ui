package co.id.rezha.mycrud.views.payungin.views.dashboard.fragments

import ShimmerHelper
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.FragmentEventsBinding
import co.id.rezha.mycrud.views.payungin.adapters.ImageSliderAdapter
import co.id.rezha.mycrud.views.payungin.adapters.LearningAdapter
import co.id.rezha.mycrud.views.payungin.data.models.responses.CategoriesEventResponse
import co.id.rezha.mycrud.views.payungin.data.networks.BaseResponsePayungin
import co.id.rezha.mycrud.views.payungin.helpers.CustomDialogBuilder
import co.id.rezha.mycrud.views.payungin.helpers.ErrorsHelper
import co.id.rezha.mycrud.views.payungin.viewmodels.EventFragmentViewModel
import co.id.rezha.mycrud.views.payungin.views.BottomSheetFilterEvent
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LearningFragment : Fragment(), BottomSheetFilterEvent.BottomSheetListener {
    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var dotsContainer: LinearLayout
    private lateinit var sliderAdapter: ImageSliderAdapter

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var llShowFilter: LinearLayout

    private lateinit var toolbar: Toolbar
    private lateinit var barFloating: LinearLayout
    private lateinit var toolbarTitle: TextView
    private lateinit var appBarLayout: AppBarLayout

    private lateinit var recyclerView: RecyclerView
    private var adapter: LearningAdapter? = null

    private lateinit var shimmerHelper: ShimmerHelper
    private lateinit var shimmerParentLayout: LinearLayout

    private lateinit var imgBackLearning: ImageView

    private val vm: EventFragmentViewModel by viewModels()

//    private listCategoriesEvent: List<CategoriesEventResponse>
    private var categoryEvents: List<CategoriesEventResponse> = emptyList()

    private var user = User("rezha", 30)


    // Implement interface bottomsheet
    override fun onDataSaved(user: User) {
      /*this.user = user
        Log.w(Consts.TAG, "user: ${user}");
        binding.inputSearch.setText("user: $user")
        Toast.makeText(requireContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show()*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewPager.unregisterOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {})
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view);

        imgBackLearning.setOnClickListener({
            requireActivity().finish()
        })

        shimmerHelper = ShimmerHelper()
        shimmerParentLayout.visibility = View.VISIBLE

        shimmerHelper.startShimmerFromParent(shimmerParentLayout)
        loadingViewPage(view)

        observeState()
        vm.categoriesEvent()

    }

    private fun initView(view: View) {
        viewPager = view.findViewById(R.id.vp2HeaderLearning)
        dotsContainer = view.findViewById(R.id.llDotsSlideLearning)

        toolbar = view.findViewById<Toolbar>(R.id.tLearning)
        barFloating = view.findViewById<LinearLayout>(R.id.llBarFloatingLearning)
        toolbarTitle = view.findViewById<TextView>(R.id.tTitleLearning)
        appBarLayout = view.findViewById<AppBarLayout>(R.id.ablLearning)

        llShowFilter = view.findViewById<LinearLayout>(R.id.llFilterLearning)
        shimmerParentLayout = view.findViewById(R.id.shimmerParentLayout)

        imgBackLearning = view.findViewById(R.id.imgBackLearning)

    }

    private fun loadingViewPage(view: View) {
        Handler(Looper.getMainLooper()).postDelayed({
            fragmentView(view);
            shimmerParentLayout.visibility = View.GONE
            shimmerHelper.stopShimmer()
        }, 60)
    }

    private fun fragmentView(view: View) {
        setupRecyclerView(view)
        setupImageSlider()

        toolbarTitle.visibility = View.GONE

        llShowFilter.setOnClickListener {
            showBottomSheet()
        }

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED ||
                    bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    return@setOnKeyListener true
                }
            }
            false
        }

        // Listener scroll
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            // Cara lebih sederhana
            val isCollapsed = Math.abs(verticalOffset) == appBarLayout.totalScrollRange

            Log.w(Consts.TAG, "verticalOffset: ${Math.abs(verticalOffset)}, totalScrollRange: ${appBarLayout.totalScrollRange}, isCollapsed: $isCollapsed")

            if (isCollapsed) {
                // Saat fully collapsed
                toolbarTitle.visibility = View.VISIBLE
                val background = context?.let { ContextCompat.getDrawable(it, R.drawable.card_template_bottoms_yellow) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    barFloating.background = background
                } else {
                    @Suppress("DEPRECATION")
                    barFloating.setBackgroundDrawable(background)
                }
            } else {
                // Saat expanded atau sedang proses collapse
                toolbarTitle.visibility = View.GONE
                val background = context?.let { ContextCompat.getDrawable(it, R.drawable.card_template_bottoms) }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    barFloating.background = background
                } else {
                    @Suppress("DEPRECATION")
                    barFloating.setBackgroundDrawable(background)
                }
            }
        })
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.categoriesEventState.collectLatest { state ->
                        when (state) {
                            is BaseResponsePayungin.Idle -> Unit
                            is BaseResponsePayungin.Loading -> {
                                //LoadingHelper.showLoadingInActivity(requireActivity())
                            }
                            is BaseResponsePayungin.Success -> {
                                //LoadingHelper.hideLoading()
                                categoryEvents = state.data
                                Log.w(Consts.TAG, "data: ${state.data}")
                            }
                            is BaseResponsePayungin.Error -> {
                                //LoadingHelper.hideLoading()
                                state.throwable?.let {
                                    ErrorsHelper(requireActivity(), it)
                                        .convertError(
                                            onBadRequestException = { errorResponse ->
                                                CustomDialogBuilder(requireActivity())
                                                    .setTitle("Informasi")
                                                    .setMessage(errorResponse?.message ?: "Request tidak valid")
                                                    .setPositiveButton("Tutup") {}
                                                    .show()
                                            },
                                        )
                                }
                                Log.e(Consts.TAG, state.message ?: "-")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rvBottomSheet)

        val itemList = listOf(
            EventsItem(
                "Promo Gadget Terbaru Promo Gadget Terbaru",
                "Diskon hingga 50% untuk semua produk",
                "https://img.freepik.com/psd-premium/desain-template-banner-promosi-gadget_70055-891.jpg"
            ),
            EventsItem(
                "Black Friday Sale Black Friday Sale",
                "Super sale dengan penawaran terbatas",
                "https://img.freepik.com/free-psd/black-friday-super-sale-facebook-cover-template_106176-1544.jpg"
            ),
            EventsItem(
                "Summer Collection Summer Collection",
                "Koleksi musim panas dengan design terbaru",
                "https://mir-s3-cdn-cf.behance.net/project_modules/fs/d1fa2f93546277.5e67adbfa56c3.jpg"
            ),
            EventsItem(
                "Winter Special Winter Special",
                "Persiapan musim dingin dengan harga spesial",
                "https://mir-s3-cdn-cf.behance.net/project_modules/fs/4a3aa993546277.5e67adbfa5df2.jpg"
            ),
            EventsItem(
                "Tech Innovation Tech Innovation",
                "Teknologi terbaru untuk kehidupan modern",
                "https://img.freepik.com/psd-premium/desain-template-banner-promosi-gadget_70055-891.jpg"
            ),
            EventsItem(
                "Lifestyle Update Lifestyle Update",
                "Tren terbaru dalam gaya hidup digital",
                "https://img.freepik.com/free-psd/black-friday-super-sale-facebook-cover-template_106176-1544.jpg"
            )
        )
        val extendedList = itemList.toMutableList()
        for (i in 7..20) {
            extendedList.add(
                EventsItem(
                    "$i Tech Innovation Lifestyle Update",
                    "Deskripsi untuk item $i dengan penawaran spesial",
                    when (i % 4) {
                        0 -> "https://img.freepik.com/psd-premium/desain-template-banner-promosi-gadget_70055-891.jpg"
                        1 -> "https://img.freepik.com/free-psd/black-friday-super-sale-facebook-cover-template_106176-1544.jpg"
                        2 -> "https://mir-s3-cdn-cf.behance.net/project_modules/fs/d1fa2f93546277.5e67adbfa56c3.jpg"
                        else -> "https://mir-s3-cdn-cf.behance.net/project_modules/fs/4a3aa993546277.5e67adbfa5df2.jpg"
                    }
                )
            )
        }

        adapter = LearningAdapter(extendedList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.isNestedScrollingEnabled = true
    }

    private fun showBottomSheet() {
        if(categoryEvents.isEmpty()){
            Toast.makeText(requireContext(), "Saat ini belum tersedia", Toast.LENGTH_LONG).show()
            return;
        }
        val bottomSheet = BottomSheetFilterEvent.newInstance(categoryEvents)
        bottomSheet.setListener(this)
        bottomSheet.show(parentFragmentManager, "BottomSheet")
    }

    private fun setupImageSlider() {
        sliderAdapter = ImageSliderAdapter()
        viewPager.adapter = sliderAdapter
        val imageUrls = listOf(
            "https://img.freepik.com/psd-premium/desain-template-banner-promosi-gadget_70055-891.jpg",
            "https://img.freepik.com/free-psd/black-friday-super-sale-facebook-cover-template_106176-1544.jpg",
            "https://mir-s3-cdn-cf.behance.net/project_modules/fs/d1fa2f93546277.5e67adbfa56c3.jpg",
            "https://mir-s3-cdn-cf.behance.net/project_modules/fs/4a3aa993546277.5e67adbfa5df2.jpg"
        )
        sliderAdapter.setImageUrls(imageUrls)
        setupDotsIndicator(imageUrls.size)
        setupAutoSlide()
    }

    private fun setupDotsIndicator(count: Int) {
        dotsContainer.removeAllViews()
        for (i in 0 until count) {
            val dotView = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    dpToPx(8), // Width 8dp
                    dpToPx(8)  // Height 8dp
                ).apply {
                    setMargins(dpToPx(4), 0, dpToPx(4), 0) // Margin horizontal 4dp
                }

                setImageDrawable(createDotDrawable(i == 0)) // First dot selected
            }
            dotsContainer.addView(dotView)
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (isAdded) {
                    updateDots(position)
                }
            }
        })
    }

    private fun createDotDrawable(isSelected: Boolean): Drawable {
        val color = if (isSelected) R.color.yellow else R.color.white
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.OVAL
        shape.setColor(ContextCompat.getColor(requireContext(), color))
        return shape
    }

    private fun updateDots(selectedPosition: Int) {
        for (i in 0 until dotsContainer.childCount) {
            val dotView = dotsContainer.getChildAt(i) as ImageView
            val isSelected = i == selectedPosition

            val drawable = createDotDrawable(isSelected)
            dotView.setImageDrawable(drawable)

            // Optional: animasi perubahan size
            val size = if (isSelected) dpToPx(10) else dpToPx(8)
            dotView.layoutParams = LinearLayout.LayoutParams(size, size).apply {
                setMargins(dpToPx(4), 0, dpToPx(4), 0)
            }
            dotView.requestLayout()
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun setupAutoSlide() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (viewPager.currentItem == sliderAdapter.itemCount - 1) {
                    viewPager.currentItem = 0
                } else {
                    viewPager.currentItem = viewPager.currentItem + 1
                }
                handler.postDelayed(this, 3000)
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 3000)
            }
        })

        // Start auto slide
        handler.postDelayed(runnable, 3000)
    }

}

data class User(
    val name: String,
    val age: Int
) : java.io.Serializable

data class EventsItem(
    val title: String,
    val subtitle: String,
    val imageUrl: String
)
