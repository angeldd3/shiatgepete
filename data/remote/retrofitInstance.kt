package com.lasec.monitoreoapp.data.remote

import com.lasec.monitoreoapp.data.remote.incident_reports_remote.CategoriesApiService
import com.lasec.monitoreoapp.data.remote.incident_reports_remote.IncidentReportsApiService
import com.lasec.monitoreoapp.data.remote.manual_workorders.AuthorizedEmployeesApiService
import com.lasec.monitoreoapp.data.remote.manual_workorders.AuthorizedVehiclesApiService
import com.lasec.monitoreoapp.data.remote.manual_workorders.DispatchVehicleTypeApiService
import com.lasec.monitoreoapp.data.remote.manual_workorders.TasksAssignmentApiService
import com.lasec.monitoreoapp.data.remote.manual_workorders.TasksPlanningApiService
import com.lasec.monitoreoapp.data.remote.manual_workorders.WorkOrdersDispatchApiService
import com.lasec.monitoreoapp.data.remote.progress_logs.ActivityStatusApiService
import com.lasec.monitoreoapp.data.remote.progress_logs.ProgressLogsApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {


    private const val BASE_URL = "https://sim.smartflow.com.mx/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .authenticator(TokenAuthenticator())
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitWithCookies by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(CookieInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
    val employeeApi: EmployeeApiService by lazy {
        retrofit.create(EmployeeApiService::class.java)
    }

    val workShiftsApi: WorkShiftApiService by lazy {
        retrofit.create(WorkShiftApiService::class.java)
    }

    val workOrdersApi: WorkOrdersApiService by lazy {
        retrofit.create(WorkOrdersApiService::class.java)
    }

    val usersApi: UsersApiService by lazy {
        retrofitWithCookies.create(UsersApiService::class.java)
    }

    val vehiclesApi: VehiclesApiService by lazy {
        retrofit.create(VehiclesApiService::class.java)
    }

    val placesApi: PlacesApiService by lazy {
        retrofit.create(PlacesApiService::class.java)
    }

    val activitiesApi: ActivitiesApiService by lazy {
        retrofit.create(ActivitiesApiService::class.java)
    }

    val categoriesApi: CategoriesApiService by lazy {
        retrofit.create(CategoriesApiService::class.java)
    }

    val dispatchVehiclesTypesApi: DispatchVehicleTypeApiService by lazy {
        retrofit.create(DispatchVehicleTypeApiService::class.java)
    }

    val authorizedVehiclesApi: AuthorizedVehiclesApiService by lazy {
        retrofit.create(AuthorizedVehiclesApiService::class.java)
    }

    val incidentReportsApi: IncidentReportsApiService by lazy {
        retrofit.create(IncidentReportsApiService::class.java)
    }

    val progressLogsApi: ProgressLogsApiService by lazy {
        retrofit.create(ProgressLogsApiService::class.java)
    }

    val activityStatusApi: ActivityStatusApiService by lazy {
        retrofit.create(ActivityStatusApiService::class.java)
    }

    val workOrdersDispatchApi: WorkOrdersDispatchApiService by lazy {
        retrofit.create(WorkOrdersDispatchApiService::class.java)
    }

    val tasksPlanningApi: TasksPlanningApiService by lazy {
        retrofit.create(TasksPlanningApiService::class.java)
    }

    val AuthorizedEmployeesApi: AuthorizedEmployeesApiService by lazy {
        retrofit.create(AuthorizedEmployeesApiService::class.java)
    }

    val TasksAssignmentApi: TasksAssignmentApiService by lazy {
        retrofit.create(TasksAssignmentApiService::class.java)
    }
}
