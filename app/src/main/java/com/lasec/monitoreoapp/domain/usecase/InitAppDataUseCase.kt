package com.lasec.monitoreoapp.domain.usecase

import android.util.Log
import com.lasec.monitoreoapp.data.EmployeesRepository
import com.lasec.monitoreoapp.data.database.entities.incident_entities.toCategoryEntity
import com.lasec.monitoreoapp.data.database.entities.incident_entities.toSubCategoryEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.toActivityTypeDispatchVehicleTypeEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.toDatabaseList
import com.lasec.monitoreoapp.data.database.entities.toDatabase
import com.lasec.monitoreoapp.data.repository.*
import com.lasec.monitoreoapp.data.repository.incident_reports_repository.CategoriesRepository
import com.lasec.monitoreoapp.data.repository.incident_reports_repository.SubCategoriesRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.ActivityTypeDispatchVehicleTypeRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.AuthorizedPlacesRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.AuthorizedVehiclesRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.DispatchVehicleTypeIndexEmployeeRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.DispatchVehicleTypesRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.WorkOrdersDispatchRepository
import com.lasec.monitoreoapp.data.repository.progress_logs_repository.ActivityStatusRepository
import javax.inject.Inject
import kotlin.collections.map

class InitAppDataUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    private val workShiftsRepository: WorkShiftsRepository,
    private val shiftRepository: ShiftRepository,
    private val placesRepository: PlacesRepository,
    private val vehiclesRepository: VehiclesRepository,
    private val categoriesRepository: CategoriesRepository,
    private val subCategoriesRepository: SubCategoriesRepository,
    private val dispatchVehiclesTypesRepository: DispatchVehicleTypesRepository,
    private val authorizedVehiclesRepository: AuthorizedVehiclesRepository,
    private val activityStatusRepository: ActivityStatusRepository,
    private val activityTypeDispatchVehicleTypeRepository: ActivityTypeDispatchVehicleTypeRepository,
    private val dispatchVehicleTypeIndexEmployeeRepository: DispatchVehicleTypeIndexEmployeeRepository,
    private val employeesRepository: EmployeesRepository,
    private val workOrdersDispatchRepository: WorkOrdersDispatchRepository,
    private val authorizedPlacesRepository: AuthorizedPlacesRepository
) {
    suspend operator fun invoke() {
        try {
            val activities = activitiesRepository.getAllActivitiesFromApi()
            val activityEntities = activities.map { it.toDatabase() }
            activitiesRepository.upsert(activityEntities)
            val result = activitiesRepository.getActivitiesByVehicleIdFromDatabase(1)
            Log.d("queryxd", "$result")
        } catch (e: Exception) {
            Log.e("InitAppDataUseCase", "❌ Error al sincronizar actividades: ${e.message}")
        }

        try {
            val shifts = workShiftsRepository.getAllWorkShiftsFromApi()
            shiftRepository.upsertShift(shifts)
        } catch (e: Exception) {
            Log.e("InitAppDataUseCase", "❌ Error al sincronizar turnos: ${e.message}")
        }

        try {
            val places = placesRepository.getAllPlacesFromApi()
            placesRepository.upsert(places)
        } catch (e: Exception) {
            Log.e("InitAppDataUseCase", "❌ Error al sincronizar lugares: ${e.message}")
        }

        try {
            val vehicles = vehiclesRepository.getAllVehiclesFromApi()
            vehiclesRepository.upsert(vehicles)
        } catch (e: Exception) {
            Log.e("InitAppDataUseCase", "❌ Error al sincronizar vehículos: ${e.message}")
        }

        try {
            val categories = categoriesRepository.getAllCategoriesFromApi()
            val categoryEntities = categories.map { it.toCategoryEntity() }
            categoriesRepository.upsertCategoriesToDatabase(categoryEntities)
        } catch (e: Exception) {
            Log.e("InitAppDataUseCase", "❌ Error al sincronizar categorias: ${e.message}")
        }

        try {
            val categories = categoriesRepository.getAllCategoriesFromApi()
            val subCategoryEntities = categories.flatMap { category ->
                category.subCategories.map { it.toSubCategoryEntity() }
            }
            subCategoriesRepository.upsertSubCategoriesToDatabase(subCategoryEntities)
        } catch (e: Exception) {
            Log.e("InitAppDataUseCase", "❌ Error al sincronizar subcategorias: ${e.message}")
        }

        try {
            val dispatchVehiclesTypes =
                dispatchVehiclesTypesRepository.getAllDispatchVehiclesTypesFromApi()
            dispatchVehiclesTypesRepository.upsertDispatchVehiclesTypes(dispatchVehiclesTypes)
        } catch (e: Exception) {
            Log.e(
                "InitAppDataUseCase",
                "❌ Error al sincronizar Dispatch Vehicles Types: ${e.message}"
            )
        }

        try {
            val authorizedVehicles = authorizedVehiclesRepository.getAllAuthorizedVehiclesFromApi()
            authorizedVehiclesRepository.clearAllAuthorizedVehiclesFromDatabase()
            authorizedVehiclesRepository.upsertAuthorizedVehiclesToDatabase(authorizedVehicles)
        } catch (e: Exception) {
            Log.e("InitAppDataUseCase", "❌ Error al sincronizar Authorized Vehicles: ${e.message}")
        }

        try {
            val activityStatus = activityStatusRepository.getAllActivityStatusFromApi()
            activityStatusRepository.upsertActivityStatus(activityStatus)
        } catch (e: Exception) {
            Log.e("InitAppDataUseCase", "❌ Error al sincronizar ActivityStatus: ${e.message}")
        }

        try {
            val activities = activitiesRepository.getAllActivitiesFromApi()
            val activityTypeDispatchVehicleTypeEntity = activities.flatMap { activity ->
                activity.activityTypeDispatchVehicleTypes.map { it.toActivityTypeDispatchVehicleTypeEntity() }
            }
            activityTypeDispatchVehicleTypeRepository.upsertActivityTypeDispatchVehicleTypeRepository(
                activityTypeDispatchVehicleTypeEntity
            )

        } catch (e: Exception) {
            Log.e("InitAppDataUseCase", "❌ Error al sincronizar actividades: ${e.message}")
        }

        try {
            val employees = employeesRepository.getAllEmployeesFromApi()

            val dispatchVehicleTypeIndexEmployeeEntity = employees.flatMap { it.toDatabaseList() }
            dispatchVehicleTypeIndexEmployeeRepository.upsertDispatchVehicleTypeIndexEmployeeToDatabase(
                dispatchVehicleTypeIndexEmployeeEntity
            )
            val result =
                dispatchVehicleTypeIndexEmployeeRepository.getVehiclesByIndexEmployeeFromDatabase(11)
            Log.d("queryxd2", "$result")
        } catch (e: Exception) {
            Log.e(
                "InitAppDataUseCase",
                "❌ Error al sincronizar dispatchVehicleTypeIndexEmployee: ${e.message}"
            )
        }

        try {
            val workOrdersDispatch = workOrdersDispatchRepository.getAllWorkOrdersDispatchFromApi()
            authorizedPlacesRepository.clearAllAuthorizedPlaces()
            authorizedPlacesRepository.insertAuthorizedPlacesToDatabase(workOrdersDispatch)
        } catch (e: Exception) {
            Log.e(
                "InitAppDataUseCase",
                "❌ Error al sincronizar AuthorizedPlacesRepository: ${e.message}"
            )
        }
    }
}
