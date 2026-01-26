package co.id.rezha.mycrud.views.AppbarCollapse

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.ActivityCollapseBinding
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseFirstFragment
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseFourthFragment
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseSecondFragment
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseThird.CollapseThirdFragment
import co.id.rezha.mycrud.views.payungin.helpers.CustomDialogBuilder
import co.id.rezha.mycrud.views.payungin.utils.setupTransparentNavigationBar
import com.google.android.material.bottomnavigation.BottomNavigationView

class CollapseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCollapseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityCollapseBinding.inflate(layoutInflater)
            window.setupTransparentNavigationBar()
            setContentView(binding.root)
            enableEdgeToEdge()
//            setupEdgeToEdge()

            // default fragment
            if (savedInstanceState == null) {
                replace(CollapseFirstFragment())
            }

            binding.bottomNavDashboard.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_dashboard -> replace(CollapseFirstFragment())
                    R.id.nav_cart -> replace(CollapseSecondFragment())
                    R.id.nav_tab -> replace(CollapseThirdFragment())
                    R.id.nav_coll_tool_image -> replace(CollapseFourthFragment())
                }
                true
            }

        }catch (e: Exception){
            Log.e(Consts.TAG, "${e.message}")
            CustomDialogBuilder(this)
                .setTitle("Informasi Dashboard")
                .setMessage("${e.message}")
                .setPositiveButton("Tutup") {}
                .show()
        }
    }

    private fun replace(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_dashboard, fragment)
            .commit()
    }

    private fun setupEdgeToEdge() {
        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_dashboard)

        // Handle insets properly
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                bottomMargin = systemBars.bottom
            }
            insets
        }

        // Apply padding to FrameLayout to avoid content behind navigation
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frame_dashboard)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, systemBars.bottom + bottomNav.height)
            insets
        }
    }
}