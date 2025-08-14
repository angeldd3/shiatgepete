package com.lasec.monitoreoapp.presentation.screens.actividad_sinorden

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskAssignmentEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskPlanningEntity
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import com.lasec.monitoreoapp.domain.model.manual_workorders.RegisterActivityParams
import com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders.RegisterAndPostWorkOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

// ManualWorkOrdersRegisterViewModel.kt (o el VM que uses para registrar)
@HiltViewModel
class ManualWorkOrdersRegisterViewModel @Inject constructor(
    private val registerAndPost: RegisterAndPostWorkOrderUseCase
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    fun registerActivity(
        indexEmployeeId: Int,
        workShiftId: Int,
        workShiftDescription: String,
        workShiftStart: String,
        workShiftEnd: String,
        indexVehicleId: Int,
        economicNumber: String,
        vehicleTypeId: Int,
        activityTypeId: Int,
        activityName: String,
        quantity: Double,
        placeId: Int,
        placeName: String,
        initTimeIso: String,
        endTimeIso: String
    ) = viewModelScope.launch {
        val params = RegisterActivityParams(
            indexEmployeeId, workShiftId, workShiftDescription,
            workShiftStart, workShiftEnd,
            indexVehicleId, economicNumber, vehicleTypeId,
            activityTypeId, activityName, quantity,
            placeId, placeName, initTimeIso, endTimeIso
        )
        // Ejecuta el flujo completo (guardar local + POST WorkOrders)
        val response = registerAndPost(params)
        // aquí puedes emitir UiState con response.workOrderId / workOrderNumber si quieres
    }
}


