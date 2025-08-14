package com.lasec.monitoreoapp.presentation.screens.actividad_sinorden

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasec.monitoreoapp.domain.model.ActivityOption
import com.lasec.monitoreoapp.domain.model.PlacesDomain
import com.lasec.monitoreoapp.domain.model.WorkShift
import com.lasec.monitoreoapp.domain.model.manual_workorders.VehicleOption
import com.lasec.monitoreoapp.domain.usecase.GetWorkshiftsUseCase
import com.lasec.monitoreoapp.domain.usecase.manual_workorders.GetActivitiesByVehicleIdUseCase
import com.lasec.monitoreoapp.domain.usecase.manual_workorders.GetUnauthorizedPlacesUseCase
import com.lasec.monitoreoapp.domain.usecase.manual_workorders.GetVehiclesByIndexEmployeeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManualWorkOrdersViewModel @Inject constructor(
    private val getVehiclesByIndexEmployeeUseCase: GetVehiclesByIndexEmployeeUseCase,
    private val getActivitiesByVehicleIdUseCase: GetActivitiesByVehicleIdUseCase,
    private val getUnauthorizedPlacesUseCase: GetUnauthorizedPlacesUseCase,
    private val getWorkshifts: GetWorkshiftsUseCase
) : ViewModel() {

    private val _vehicles = MutableLiveData<List<VehicleOption>>()
    val vehicles: LiveData<List<VehicleOption>> = _vehicles

    private val _activities = MutableLiveData<List<ActivityOption>>()
    val activities: LiveData<List<ActivityOption>> = _activities

    private val _unauthorizedPlaces = MutableLiveData<List<PlacesDomain>>()
    val unauthorizedPlaces: LiveData<List<PlacesDomain>> = _unauthorizedPlaces

    private val _currentShift = MutableLiveData<WorkShift?>()
    val currentShift: LiveData<WorkShift?> = _currentShift

    fun loadVehicles(indexEmployeeId: Int) {
        viewModelScope.launch {
            val result = getVehiclesByIndexEmployeeUseCase(indexEmployeeId)
            _vehicles.value = result
        }
    }

    fun loadActivities(indexVehicleId: Int) {
        viewModelScope.launch {
            val result = getActivitiesByVehicleIdUseCase(indexVehicleId)
            _activities.value = result
        }
    }

    fun loadUnauthorizedPlaces() {
        viewModelScope.launch {
            val result = getUnauthorizedPlacesUseCase()
            _unauthorizedPlaces.value = result
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadCurrentShift() = viewModelScope.launch {
        _currentShift.value = getWorkshifts.getTurnoActual()
    }
}