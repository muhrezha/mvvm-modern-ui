package co.id.rezha.myroomdb.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import co.id.rezha.myroomdb.R
import co.id.rezha.core.utlis.defaultAppDatabase
import co.id.rezha.core.data.db.AppDatabase
import co.id.rezha.myroomdb.databinding.RoomActivityMainBinding

class MainActivity : AppCompatActivity() {
//    private lateinit var binding: RoomActivityMainBinding


    private var mDb: AppDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = RoomActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
        enableEdgeToEdge()
        setContentView(R.layout.room_activity_main)

        mDb = defaultAppDatabase

//        binding.btnListView.setOnClickListener {
//            startActivity(Intent(this, ListViewActivity::class.java))
//        }

        val btnListView = findViewById<Button>(R.id.btnListView)
        btnListView.setOnClickListener({
            val intent = Intent(this, ListViewActivity::class.java)
            startActivity(intent)
        })

//        binding.btnListView.setOnClickListener{
//            startActivity(Intent(this,  RecycleViewActivity::class.java))
//        }

//        lifecycleScope.launch {
//            val tableUser = TableUser(
//                name = "Rezha",
//                age = 25,
//                gender = "F",
//            )
//            mDb?.userDao()?.insert(tableUser)
//            mDb?.userDao()?.getAllUser().let {
//                Log.w(Consts.TAG, "Data: ${it.toString()}")
//            }
//        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
}