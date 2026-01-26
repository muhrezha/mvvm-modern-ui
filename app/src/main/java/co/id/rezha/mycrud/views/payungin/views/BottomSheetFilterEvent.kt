package co.id.rezha.mycrud.views.payungin.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.views.payungin.adapters.BottomSheetAdapter
import co.id.rezha.mycrud.views.payungin.data.models.responses.CategoriesEventResponse
import co.id.rezha.mycrud.views.payungin.views.dashboard.fragments.User
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFilterEvent : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private var adapter: BottomSheetAdapter? = null

    private var listCategory: List<CategoriesEventResponse>? = emptyList()

    // Interface untuk komunikasi dengan Activity/Fragment
    interface BottomSheetListener {
        fun onDataSaved(user: User)
    }

    private var listener: BottomSheetListener? = null
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var btnSave: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        setupBottomSheetBehavior()
        populateDataFromArguments(view)

    }

    override fun onStart() {
        super.onStart()

        // Atur window agar tidak fullscreen penuh
        dialog?.window?.let { window ->
            // Dapatkan tinggi status bar
            val statusBarHeight = getStatusBarHeight()
            val toolbarHeight = getToolbarHeight()
//            val totalTopMargin = statusBarHeight + toolbarHeight
            val totalTopMargin = 100

            // Atur decor view padding
            window.decorView.setPadding(0, totalTopMargin, 0, 0)

            // Atau atur window dimensions
            val displayMetrics = resources.displayMetrics
            val layoutParams = window.attributes
            layoutParams.height = displayMetrics.heightPixels - totalTopMargin
            layoutParams.y = totalTopMargin // Posisikan dari atas dengan margin
            window.attributes = layoutParams
        }
    }

    private fun getToolbarHeight(): Int {
        val styledAttributes = requireContext().theme.obtainStyledAttributes(
            intArrayOf(android.R.attr.actionBarSize)
        )
        val toolbarHeight = styledAttributes.getDimensionPixelSize(0, 0)
        styledAttributes.recycle()
        return toolbarHeight
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun setupViews(view: View) {
        etName = view.findViewById(R.id.etName)
        etAge = view.findViewById(R.id.etAge)
        btnSave = view.findViewById(R.id.btnSave)

        btnSave.setOnClickListener {
            saveData()
        }
    }

    private fun populateDataFromArguments(view: View) {
        // Ambil data dari arguments
        listCategory = arguments?.getSerializable("CATEGORIES_EVENT") as? List<CategoriesEventResponse>
        setupRecyclerView(view)

//        user?.let {
//            etName.setText(it.name)
//            etAge.setText(it.age.toString())
//        }
    }

    fun setListener(listener: BottomSheetListener) {
        this.listener = listener
    }

    private fun saveData() {
        val name = etName.text.toString()
        val ageText = etAge.text.toString()
        if (name.isBlank() || ageText.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        val age = ageText.toIntOrNull() ?: 0
        val updatedUser = User(name, age)
        listener?.onDataSaved(updatedUser)
        dismiss()
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rvBottomSheet)

        val itemList = mutableListOf<String>()
        for (i in 0 until listCategory?.size!!) {
            Log.w(Consts.TAG, "Data: ${listCategory!![i].nameId}")
            itemList.add("Item ${listCategory!![i].nameId}")
        }

        adapter = BottomSheetAdapter(itemList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.isNestedScrollingEnabled = true
    }

    private fun setupBottomSheetBehavior() {
        val bottomSheet = dialog as? BottomSheetDialog
        val behavior = bottomSheet?.behavior

        behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        behavior?.isHideable = true
        behavior?.isFitToContents = false

        val displayMetrics = resources.displayMetrics
        val halfScreenHeight = (displayMetrics.heightPixels * 0.5).toInt()
        behavior?.peekHeight = halfScreenHeight

        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.d("BottomSheet", "State: EXPANDED")
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.d("BottomSheet", "State: COLLAPSED")
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Handle slide animation
            }
        })
    }

    // Attach listener ketika fragment attached ke activity
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is BottomSheetListener) {
//            listener = context
//        } else if (parentFragment is BottomSheetListener) {
//            listener = parentFragment as BottomSheetListener
//        }
//    }

    // Detach listener
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        fun newInstance(categories: List<CategoriesEventResponse>): BottomSheetFilterEvent {
            val fragment = BottomSheetFilterEvent()
            val args = Bundle()
            args.putSerializable("CATEGORIES_EVENT", categories as ArrayList)
            fragment.arguments = args
            return fragment
        }
    }
}