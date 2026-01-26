package co.id.rezha.mycrud.views.payungin.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.id.rezha.mycrud.views.payungin.data.models.responses.CategoriesEventResponse
import co.id.rezha.mycrud.views.payungin.data.networks.BaseResponsePayungin
import co.id.rezha.mycrud.views.payungin.data.repositories.PayunginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = PayunginRepository(application)

    private val _categoriesEventState = MutableStateFlow<BaseResponsePayungin<List<CategoriesEventResponse>>>(BaseResponsePayungin.Idle)
    val categoriesEventState: StateFlow<BaseResponsePayungin<List<CategoriesEventResponse>>> = _categoriesEventState

    fun categoriesEvent() {
        _categoriesEventState.value = BaseResponsePayungin.Loading
        viewModelScope.launch {
            try {
                val data = repo.categoriesEvent()
                _categoriesEventState.value = BaseResponsePayungin.Success(data)
            } catch (e: Exception) {
                _categoriesEventState.value = BaseResponsePayungin.Error(message = e.message ?: "Unknown error", throwable = e)
            }
        }
    }
}