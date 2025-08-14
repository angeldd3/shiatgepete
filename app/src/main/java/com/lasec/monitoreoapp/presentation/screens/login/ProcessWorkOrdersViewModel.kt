package com.lasec.monitoreoapp.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasec.monitoreoapp.domain.usecase.ProcessAndSaveWorkOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@HiltViewModel
class ProcessWorkOrdersViewModel @Inject constructor(
    private val processWorkOrdersUseCase: ProcessAndSaveWorkOrdersUseCase

) : ViewModel() {
    private val _ordenesProcesadas = MutableLiveData<Boolean>()
    val ordenesProcesadas: LiveData<Boolean> = _ordenesProcesadas
    private val _resultado = MutableLiveData<Result<Unit>>()
    val resultado: LiveData<Result<Unit>> = _resultado

    fun procesarOrdenesParaEmpleado(dispatchEmployeeId: Int) {
        viewModelScope.launch {
            try {
                processWorkOrdersUseCase(dispatchEmployeeId)
                _ordenesProcesadas.postValue(true)
                _resultado.value = Result.success(Unit)
            } catch (e: Exception) {
                _resultado.value = Result.failure(e)
            }
        }
    }


}
