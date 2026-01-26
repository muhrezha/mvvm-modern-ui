package co.id.rezha.mycrud.views.fragment_case.fragments

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val nama: String,
    val usia: Int
) : Parcelable

