package co.id.rezha.mycrud.views.payungin.views.dashboard

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.ActivityDashboardPayunginBinding
import co.id.rezha.mycrud.views.payungin.helpers.CustomDialogBuilder
import co.id.rezha.mycrud.views.payungin.utils.setupTransparentNavigationBar
import co.id.rezha.mycrud.views.payungin.views.dashboard.fragments.LearningFragment
import co.id.rezha.mycrud.views.payungin.views.dashboard.fragments.JobsFragment
import co.id.rezha.mycrud.views.payungin.views.dashboard.fragments.HomeFragment

class DashboardPayunginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardPayunginBinding

    private var currentFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            supportActionBar?.hide()
            binding = ActivityDashboardPayunginBinding.inflate(layoutInflater)
            window.setupTransparentNavigationBar()
            setContentView(binding.root)
            enableEdgeToEdge()

            // default fragment
            /*if (savedInstanceState == null) {
                replace(HomeFragment())
            }*/

            /*binding.bottomNavDashboard.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_dashboard -> replace(HomeFragment())
                    R.id.nav_jobs -> replace(JobsFragment())
                    R.id.nav_learning -> replace(LearningFragment())
                }
                true
            }*/

            // no reload fragment
            // Inisialisasi fragment pertama kali
            if (savedInstanceState == null) {
                showFragment(HomeFragment(), "HomeFragment")
            }

            binding.bottomNavDashboard.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_dashboard -> showFragment(HomeFragment(), "HomeFragment")
                    R.id.nav_jobs -> showFragment(JobsFragment(), "JobsFragment")
                    R.id.nav_learning -> showFragment(LearningFragment(), "LearningFragment")
                }
                true
            }

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                    insets
                }
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

    private fun showFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        // Cek apakah fragment sudah ada
        val existingFragment = fragmentManager.findFragmentByTag(tag)

        if (existingFragment != null) {
            // Jika fragment sudah ada, show fragment yang dipilih dan hide yang lain
            if (currentFragment != null) {
                transaction.hide(currentFragment!!)
            }
            transaction.show(existingFragment)
            currentFragment = existingFragment
        } else {
            // Jika fragment belum ada, buat baru
            if (currentFragment != null) {
                transaction.hide(currentFragment!!)
            }
            transaction.add(R.id.frame_dashboard, fragment, tag)
            currentFragment = fragment
        }

        transaction.commit()
    }

}