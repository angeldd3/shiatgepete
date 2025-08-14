package com.lasec.monitoreoapp.presentation.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasec.monitoreoapp.domain.model.WorkShift
import com.lasec.monitoreoapp.domain.usecase.GetWorkshiftsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkShiftViewModel @Inject constructor(
    private val getWorkshiftsUseCase: GetWorkshiftsUseCase
) : ViewModel() {

    val turnoActual = MutableLiveData<WorkShift?>()

    fun obtenerTurnoActual() {
        viewModelScope.launch {
            val turno = getWorkshiftsUseCase.getTurnoActual()
            turnoActual.value = turno
        }
    }
}

