package com.lasec.monitoreoapp.presentation.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasec.monitoreoapp.domain.model.WorkOrder
import com.lasec.monitoreoapp.domain.usecase.GetWorkOrdersForEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkOrderViewModel @Inject constructor(
    private val getWorkOrdersForEmployeeUseCase: GetWorkOrdersForEmployeeUseCase
) : ViewModel() {

    private val _ordenesFiltradas = MutableLiveData<List<WorkOrder>>()
    val ordenesFiltradas: LiveData<List<WorkOrder>> = _ordenesFiltradas

    fun obtenerOrdenesFiltradas(dispatchEmployeeId: Int) {
        viewModelScope.launch {
            val result = getWorkOrdersForEmployeeUseCase(dispatchEmployeeId)
            _ordenesFiltradas.postValue(result)
        }
    }
}
