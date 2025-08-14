package com.lasec.monitoreoapp.presentation.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasec.monitoreoapp.domain.usecase.InitAppDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitViewModel @Inject constructor(
    private val initAppDataUseCase: InitAppDataUseCase
) : ViewModel() {

    private val _initState = MutableLiveData<InitState>()
    val initState: LiveData<InitState> = _initState

    fun initAppData() {
        viewModelScope.launch {
            try {
                _initState.value = InitState.Loading
                initAppDataUseCase()
                _initState.value = InitState.Success
            } catch (e: Exception) {
                _initState.value = InitState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

sealed class InitState {
    object Loading : InitState()
    object Success : InitState()
    data class Error(val message: String) : InitState()
}