package co.id.rezha.mycrud

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycompose.ui.activities.MainComposeActivity
import co.id.rezha.mycrud.models.UserTest
import co.id.rezha.mycrud.views.AppbarCollapse.CollapseActivity
import co.id.rezha.mycrud.views.payungin.views.LoginActivity
import co.id.rezha.mycrud.views.custom_nav_drawer_case.NavDrawerActivity
import co.id.rezha.mycrud.views.fragment_case.FragmentActivity
import co.id.rezha.mycrud.views.grid_case.GridActivity
import co.id.rezha.mycrud.views.setting_permission.PermissionManager
import co.id.rezha.mycrud.views.setting_permission.SettingActivity
import co.id.rezha.mycrud.views.sqlite.ui.activities.ProductActivity
import co.id.rezha.mycrud.views.tabbar_case.MainTabActivity
import co.id.rezha.myroomdb.ui.MainActivity

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityMainBinding

    private lateinit var btnIncrement: Button
    private lateinit var btnDecrement: Button
    private lateinit var btnReqCamera: Button
    private lateinit var btnReqGallery: Button

    private lateinit var btnKotlinCompose: Button
    private lateinit var btnLogin: Button
    private lateinit var btnFragment: Button
    private lateinit var btnGrid: Button
    private lateinit var btnNavDrawer: Button
    private lateinit var btnTabbar: Button

    private lateinit var btnAppBar: Button
    private lateinit var btnRoomDb: Button
    private lateinit var btnSqlite: Button
    private lateinit var btnSettingPermission: Button

    private lateinit var tvOne: TextView
    private lateinit var tvTwo: TextView


    private var counterOne = 0
    private var counterTwo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        try {
            initViews()
            setupClickListeners()

            btnReqCamera.setOnClickListener {
                if (PermissionManager.instance.hasCameraPermission(this)) {
                    Toast.makeText(this, "Camera Permission Granted!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Need req permission", Toast.LENGTH_LONG).show()
                }
            }

            btnReqGallery.setOnClickListener {
                if (PermissionManager.instance.hasGalleryPermission(this)) {
                    Toast.makeText(this, "Gallery Permission Granted!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Need req permission", Toast.LENGTH_LONG).show()
                }
            }

            btnLogin.setOnClickListener({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            })

            btnKotlinCompose.setOnClickListener({
                val intent = Intent(this, MainComposeActivity::class.java)
                startActivity(intent)
            })

            btnGrid.setOnClickListener({
                val intent = Intent(this, GridActivity::class.java)
                startActivity(intent)
            })

            btnNavDrawer.setOnClickListener({
                val intent = Intent(this, NavDrawerActivity::class.java)
                intent.putExtra("paramFromMainActivity", "Goo!!!!!!!!!!!")
                startActivity(intent)
            })

            btnTabbar.setOnClickListener({
                val intent = Intent(this, MainTabActivity::class.java)
                startActivity(intent)
            })

            /*Class ini tidak ada diprohect :app, tetapi bisa dipanggil karena
                implementation(project(":myroomdb")) di gradle :app.
             code ref: 001-roomdb*/
            btnAppBar.setOnClickListener({
                val intent = Intent(this, CollapseActivity::class.java)
                startActivity(intent)
            })

            btnRoomDb.setOnClickListener({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            })

            btnSqlite.setOnClickListener({
                val intent = Intent(this, ProductActivity::class.java)
                startActivity(intent)
            })

            btnSettingPermission.setOnClickListener({
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            })


        } catch (e: Exception) {
            Log.e(Consts.TAG, "Exception: ${e.message}", e)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        btnIncrement = findViewById(R.id.btn_increment)
        btnDecrement = findViewById(R.id.btn_decrement)
        btnReqGallery = findViewById(R.id.btnReqGallery)
        btnReqCamera = findViewById(R.id.btnReqCamera)

        tvOne = findViewById(R.id.tv_one)
        tvTwo = findViewById(R.id.tv_two)

        btnKotlinCompose = findViewById(R.id.btnKotlinCompose)
        btnLogin = findViewById(R.id.btn_login)
        btnFragment = findViewById(R.id.btn_fragment)
        btnGrid = findViewById(R.id.btn_grid)
        btnNavDrawer = findViewById(R.id.btn_nav_drawer)
        btnTabbar = findViewById(R.id.btn_tabbar)

        btnAppBar = findViewById(R.id.btnAppbar)
        btnRoomDb = findViewById(R.id.btnRoomDb)
        btnSqlite = findViewById(R.id.btnSqlite)
        btnSettingPermission = findViewById(R.id.btnSetting)
    }

    private fun setupClickListeners() {
        btnIncrement.setOnClickListener {
            counterOne++
            tvOne.text = counterOne.toString()
        }

        btnDecrement.setOnClickListener {
            counterTwo--
            tvTwo.text = counterTwo.toString()
        }

        tvOne.setOnClickListener {
            counterOne = 0
            tvOne.text = "0"
        }

        tvTwo.setOnClickListener {
            counterTwo = 0
            tvTwo.text = "0"
        }

        btnFragment.setOnClickListener({
            val intent = Intent(this, FragmentActivity::class.java)
            intent.putExtra("paramFromMainActivity", "Fragment!")
            startActivity(intent)
        })

    }

    private fun printUserInfo(user: UserTest) {
        Log.w(Consts.TAG, "=== USER INFO ===")
        Log.w(Consts.TAG, user.getUserInfo())
        Log.w(Consts.TAG, "=================")
        Toast.makeText(
            this,
            "User Created: ${user.getUserInfo()}",
            Toast.LENGTH_LONG
        ).show()
    }

}//End