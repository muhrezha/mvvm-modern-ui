package co.id.rezha.mycrud.views.payungin.data.models.responses

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//@Parcelize
data class CategoriesEventResponse(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name_id") var nameId: String? = null,
    @SerializedName("sub_category") var subCategory: ArrayList<SubCategory> = arrayListOf()
) : java.io.Serializable

//@Parcelize
data class SubCategory(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name_id") var nameId: String? = null
) : Serializable