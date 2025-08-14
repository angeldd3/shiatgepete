package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders

import android.os.Build
import androidx.annotation.RequiresApi
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskAssignmentEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskPlanningEntity
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import com.lasec.monitoreoapp.domain.extensions.manual_workorders.MX_ZONE
import com.lasec.monitoreoapp.domain.extensions.manual_workorders.toUtcIsoZ
import com.lasec.monitoreoapp.domain.model.manual_workorders.RegisterActivityParams
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class RegisterActivityLocalUseCase @Inject constructor(
    private val repo: ManualWorkOrdersRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(p: RegisterActivityParams): Int {
        val f = DateTimeFormatter.ofPattern("HH:mm[:ss]")
        val startLT = LocalTime.parse(p.workShiftStart, f)
        val endLT   = LocalTime.parse(p.workShiftEnd, f)

        val workDate = LocalDate.now(MX_ZONE)

        val initUtcIso = p.initTimeLocal.toUtcIsoZ(workDate)
        val endUtcIso  = p.endTimeLocal.toUtcIsoZ(workDate)

        val assignment = TaskAssignmentEntity(
            indexEmployeeId = p.indexEmployeeId,
            cloned = false,
            synced = false,
            workShiftId = p.workShiftId,
            workShiftDescription = p.workShiftDescription,
            workShiftStartHour = startLT.hour,
            workShiftStartMinute = startLT.minute,
            workShiftStartSecond = startLT.second,
            workShiftEndHour = endLT.hour,
            workShiftEndMinute = endLT.minute,
            workShiftEndSecond = endLT.second
        )

        var newId = 0
        repo.insertAssignmentWithPlanning(assignment) { assignmentLocalId ->
            newId = assignmentLocalId
            TaskPlanningEntity(
                assignmentLocalId = assignmentLocalId,
                indexVehicleId = p.indexVehicleId,
                economicNumber = p.economicNumber,
                vehicleTypeId = p.vehicleTypeId,
                activityTypeId = p.activityTypeId,
                activityName = p.activityName,
                quantity = p.quantity,
                placeId = p.placeId,
                placeName = p.placeName,
                initTime = initUtcIso,
                endTime = endUtcIso
            )
        }

        return newId
    }
}

