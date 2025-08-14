package com.lasec.monitoreoapp.domain.usecase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.lasec.monitoreoapp.data.database.entities.OrdersEntity
import com.lasec.monitoreoapp.data.database.entities.TasksEntity
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntityFromTasks
import com.lasec.monitoreoapp.data.database.entities.toDatabase
import com.lasec.monitoreoapp.data.repository.*
import com.lasec.monitoreoapp.data.repository.progress_logs_repository.ProgressLogRepositoryFromTasks
import java.time.LocalDate
import javax.inject.Inject

class ProcessAndSaveWorkOrdersUseCase @Inject constructor(
    private val workShiftsUseCase: GetWorkshiftsUseCase,
    private val workOrdersRepository: WorkOrdersRepository,
    private val usersRepository: UsersRepository,
    private val ordersRepository: OrdersRepository,
    private val tasksRepository: TasksRepository,
    private val progressLogRepositoryFromTasks: ProgressLogRepositoryFromTasks

) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(dispatchEmployeeId: Int) {
        try {
            Log.d(
                "Proceso",
                "🔧 Iniciando procesamiento de órdenes para empleado $dispatchEmployeeId"
            )

            val turnoActual = workShiftsUseCase.getTurnoActual() ?: run {
                Log.e("Proceso", "⚠️ Turno actual no encontrado. Abortando proceso.")
                return
            }

            // Aqui se establece la fecha que se utilizara para cosultar ordenes de trabajo
            val today = LocalDate.now()
            val createdAt = "${today.year}-${today.monthValue}-${today.dayOfMonth}"
            Log.d("CREATEDAT", "Aqui: ${createdAt}")
            Log.d("CREATEDAT", "Aca: ${turnoActual.id}")

            val ordenes = workOrdersRepository.getWorkOrders(turnoActual.id, createdAt)
//            val ordenes = workOrdersRepository.getWorkOrders(1, "2025-8-5")

            Log.d("Proceso", "✅ Órdenes totales obtenidas: ${ordenes.size}")

            val ordenesFiltradas = ordenes.map { orden ->
                orden.copy(
                    placeWorkOrders = orden.placeWorkOrders.orEmpty().map { pwo ->
                        pwo.copy(
                            taskPlannings = pwo.taskPlannings.orEmpty().filter { tp ->
                                tp.taskAssignment.indexEmployee.dispatchEmployeeId == dispatchEmployeeId
                            }
                        )
                    }.filter { it.taskPlannings?.isNotEmpty() == true }
                )
            }.filter { it.placeWorkOrders?.isNotEmpty() == true }


            Log.d("Proceso", "✅ Órdenes después de filtrar por empleado: ${ordenesFiltradas}")


            for (orden in ordenesFiltradas) {
                Log.d("Proceso", "➡️ Procesando orden: ${orden.workOrderNumber}")

                // Guardar turno
//                val turno = workShiftsRepository.getById(orden.workShiftId)
//                val newShiftId = shiftRepository.upsertShift(turno).toInt()
//                Log.d("Proceso", "🕐 Turno guardado con ID local: $newShiftId")

                // Guardar usuario


                val user = try {
                    orden.userId.let { id -> usersRepository.getUserById(id) }
                } catch (e: Exception) {
                    Log.e("Proceso", "⚠️ Error al obtener usuario ${orden.userId}: ${e.message}")
                    null
                }

                if (user != null) {
                    usersRepository.upsertUsers(user.toDatabase())
                    Log.d("Proceso", "👤 Usuario insertado (o ya existente) con ID: ${user.id}")
                } else {
                    Log.w(
                        "Proceso",
                        "⚠️ Usuario nulo, se continuará sin guardar usuario para esta orden."
                    )
                }


                // Insertar orden
                val ordenId = ordersRepository.upsert(
                    OrdersEntity(
                        id = orden.workOrderId,
                        workOrderNumber = orden.workOrderNumber,
                        createdAt = orden.createdAt,
                        shiftId = orden.workShiftId,
                        userId = user?.id  // ← ¡Asegurado!
                    )
                )
                Log.d("Proceso", "📄 Orden insertada con ID local: $ordenId")


                // Procesar tareas
                for (pwo in orden.placeWorkOrders) {
                    for (tarea in pwo.taskPlannings) {
//                        val vehicleRoomId =
//                            vehiclesRepository.getByIndexVehicleId(tarea.indexVehicleId)?.let {
//                                val id = vehiclesRepository.upsert(it.toDatabase())
//                                Log.d("Proceso", "🚜 Vehículo insertado con ID: $id")
//                                id
//                            }?.toInt() ?: continue

//                        val placeRoomId =
//                            placesRepository.getAllPlacesByIdFromApi(pwo.placeId)?.let {
//                                val id = placesRepository.upsert(it.toDatabase())
//                                Log.d("Proceso", "📍 Lugar insertado con ID: $id")
//                                id
//                            }?.toInt() ?: continue

//                        val activityRoomId =
//                            activitiesRepository.getActivitiesByIdFromApi(tarea.activityTypeId)
//                                ?.let {
//                                    val id = activitiesRepository.upsert(it.toDatabase())
//                                    Log.d("Proceso", "🔨 Actividad insertada con ID: $id")
//                                    id
//                                }?.toInt() ?: continue

                        tasksRepository.upsert(
                            TasksEntity(
                                orderId = orden.workOrderId,
                                taskPlanningId = tarea.taskPlanningId,
                                activityId = tarea.activityTypeId,
                                placeId = pwo.placeId,
                                vehicleId = tarea.indexVehicleId,
                                quantity = tarea.quantity.toFloat(),
                                initTime = tarea.initTime,
                                endTime = tarea.endTime,
                                dispatchEmployeeId = tarea.taskAssignment.indexEmployee.dispatchEmployeeId
                            )
                        )

                        Log.d("Proceso", "✅ Tarea insertada correctamente para orden ID $ordenId")

                        // Inserta progressLog si existe
                        tarea.progressLog?.let { progress ->
                            progressLogRepositoryFromTasks.upsertProgressLogFromTasksToDatabase(
                                ProgressLogEntityFromTasks(
                                    progressLogId = progress.progressLogId,
                                    taskPlanningId = progress.taskPlanningId,
                                    activityTypeDepartmentOriginWorkOrderId = progress.activityTypeDepartmentOriginWorkOrderId,
                                    activityStatusId = progress.activityStatusId,
                                    percentage = progress.percentage,
                                    quantity = progress.quantity,
                                    createdAt = progress.createdAt
                                )
                            )
                            Log.d(
                                "Proceso",
                                "📈 ProgressLog insertado para taskPlanningId ${progress.taskPlanningId}"
                            )
                        }
                    }
                }
            }

            Log.d("Proceso", "🎉 Finalizó el procesamiento de órdenes")
        } catch (e: Exception) {
            Log.e("Proceso", "❌ Error inesperado: ${e.message}", e)
        }
    }
}
