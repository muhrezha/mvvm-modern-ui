package co.id.rezha.mycrud.views.custom_nav_drawer_case

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.ActivityNavDrawerBinding
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import java.util.Arrays

class NavDrawerActivity : AppCompatActivity(), DuoMenuView.OnMenuClickListener {

    private lateinit var binding: ActivityNavDrawerBinding
    private lateinit var tvParam: TextView

    private var mMenuAdapter: MenuAdapter? = null
    private var mViewHolder: ViewHolder? = null
    private var mTitles = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            supportActionBar?.hide()
            enableEdgeToEdge()
            binding = ActivityNavDrawerBinding.inflate(layoutInflater)
            setContentView(binding.root)
//            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//                insets
//            }
//            val message = intent.getStringExtra("paramFromMainActivity")
//            initViews()
//            message?.let {
//                tvParam.text = it
//            }


            mTitles = ArrayList(Arrays.asList(*resources.getStringArray(R.array.menuOptionsNavDrawer)))

            // Initialize the views
            mViewHolder = ViewHolder()

            // Handle toolbar actions
            handleToolbar()

            // Handle menu actions
            handleMenu()

            // Handle drawer actions
            handleDrawer()

            // Show main fragment in container
            goToFragment(MainFragment(), false)
            mMenuAdapter!!.setViewSelected(0, true)
            title = mTitles[0]


        }catch (e: Exception){
            Log.e(Consts.TAG, "Exception: ${e.message}", e)
        }

    }

    private fun initViews(){
//        tvParam = binding.tvParam
    }

    private fun handleToolbar() {
        setSupportActionBar(mViewHolder!!.mToolbar)
    }

    private fun handleDrawer() {
        val DrawerToggle = DuoDrawerToggle(
            this,
            mViewHolder!!.mDrawerLayout,
            mViewHolder!!.mToolbar,
            R.string.app_name,
            R.string.app_name
        )
        mViewHolder!!.mDrawerLayout.setDrawerListener(DrawerToggle)
        DrawerToggle.syncState()
    }

    private fun handleMenu() {
        mMenuAdapter = MenuAdapter(mTitles)
        mViewHolder!!.mDuoMenuView.setOnMenuClickListener(this)
        mViewHolder!!.mDuoMenuView.adapter = mMenuAdapter
    }

    override fun onFooterClicked() {
        Toast.makeText(this, "onFooterClicked", Toast.LENGTH_SHORT).show()
    }

    override fun onHeaderClicked() {
        Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show()
    }

    private fun goToFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.add(R.id.container, fragment).commit()
    }

    override fun onOptionClicked(position: Int, objectClicked: Any) {
        // Set the toolbar title
        title = mTitles[position]

        // Set the right options selected
        mMenuAdapter!!.setViewSelected(position, true)
        when (position) {
            else -> goToFragment(MainFragment(), false)
        }

        // Close the drawer
        mViewHolder!!.mDrawerLayout.closeDrawer()
    }

    private inner class ViewHolder internal constructor() {
        val mDrawerLayout: DuoDrawerLayout
        val mDuoMenuView: DuoMenuView
        val mToolbar: Toolbar

        init {
            mDrawerLayout = findViewById<View>(R.id.drawer) as DuoDrawerLayout
            mDuoMenuView = mDrawerLayout.menuView as DuoMenuView
            mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        }
    }


}// End