package co.id.rezha.myroomdb.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import co.id.rezha.core.data.db.AppDatabase
import co.id.rezha.core.data.tables.TableUser
import co.id.rezha.core.helpers.Consts
import co.id.rezha.core.utlis.defaultAppDatabase
import co.id.rezha.myroomdb.R
import co.id.rezha.myroomdb.databinding.RoomActivityListViewBinding
import co.id.rezha.myroomdb.interfaces.UserCallback
import co.id.rezha.myroomdb.ui.adapters.UserAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ListViewActivity : AppCompatActivity(), UserCallback {
    private lateinit var binding: RoomActivityListViewBinding

    private lateinit var adapter: UserAdapter
    private var mDb: AppDatabase? = null
    private var userList = mutableListOf<TableUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = RoomActivityListViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.room_activity_list_view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mDb = defaultAppDatabase

        val fabAddUser = findViewById<FloatingActionButton>(R.id.fabAddUser)

        fabAddUser.setOnClickListener {
            addRandomUser()
        }

        lifecycleScope.launch {
            loadUsers()
        }
    }


    private fun addRandomUser() {
        lifecycleScope.launch {
            val randomAge = (18..40).random()
            val tableUser = TableUser(
                name = "User ${System.currentTimeMillis()}",
                age = randomAge
            )
            mDb?.userDao()?.insert(tableUser)
            loadUsers()
        }
    }


    private fun loadUsers() {
        Log.w(Consts.TAG, "Load users")
        userList = mDb?.userDao()?.getAllUser()?.toMutableList() ?: mutableListOf()
        adapter = UserAdapter(this, userList, this)
        binding.listViewUser.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun showEditDialog(user: TableUser) {
        val dialogView = layoutInflater.inflate(R.layout.room_dialog_edit_user, null)

        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etAge = dialogView.findViewById<EditText>(R.id.etAge)
        val etGender = dialogView.findViewById<EditText>(R.id.etGender)

        etName.setText(user.name)
        etAge.setText(user.age.toString())
        etGender.setText(user.gender)

        AlertDialog.Builder(this)
            .setTitle("Edit User")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updated = user.copy(
                    name = etName.text.toString(),
                    age = etAge.text.toString().toIntOrNull() ?: 0,
                    gender = etGender.text.toString()
                )

                lifecycleScope.launch {
                    mDb?.userDao()?.update(updated)
                    refreshList()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    // Refresh list without reload Activity
    private fun refreshList() {
        val newList = mDb?.userDao()?.getAllUser() ?: emptyList()
        adapter.updateList(newList)

        Log.w(Consts.TAG, "Updated list: $newList")
    }


    override fun onEditClicked(user: TableUser) {
        showEditDialog(user)
    }
}