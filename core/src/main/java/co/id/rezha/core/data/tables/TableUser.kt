package co.id.rezha.core.data.tables

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class TableUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @Expose
    val name: String,

    @Expose
    val age: Int = 0,

    @Expose
    val gender: String? = null


) : Parcelable
