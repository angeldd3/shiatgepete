package com.lasec.monitoreoapp.presentation.screens.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Filter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lasec.monitoreoapp.R
import com.lasec.monitoreoapp.auth.TokenManager
import com.lasec.monitoreoapp.core.util.SessionManager
import com.lasec.monitoreoapp.domain.model.Employee
import com.lasec.monitoreoapp.presentation.screens.home.HomeActivity
import com.lasec.monitoreoapp.presentation.screens.home.WorkOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var numeroEdit: EditText
    private lateinit var loginBtn: Button

    private lateinit var autoCompleteEmpleado: AutoCompleteTextView

    private var empleadoSeleccionado: Employee? = null


    private val viewModel: LoginViewModel by viewModels()
    private val employeeViewModel: EmployeeViewModel by viewModels()

    private val workShiftViewModel: WorkShiftViewModel by viewModels()

    private val workOrderViewModel: WorkOrderViewModel by viewModels()

    private val processWorkOrdersViewModel: ProcessWorkOrdersViewModel by viewModels()

    private val initViewModel: InitViewModel by viewModels()


    private suspend fun waitForToken(timeoutMs: Long = 5000): String? {
        val start = System.currentTimeMillis()
        while (System.currentTimeMillis() - start < timeoutMs) {
            val token = TokenManager.getAccessToken()
            if (token.isNotBlank()) {
                return token
            }
            delay(200)
        }
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        esconderBarras()
        supportActionBar?.hide()

        setContentView(R.layout.activity_login)

        lifecycleScope.launch {
            val token = waitForToken()
            if (token != null) {
                Log.d("LoginActivity", "✅ Token obtenido, iniciando sincronización")
                initViewModel.initAppData() // aquí ya puedes lanzar tu lógica segura
                employeeViewModel.onCreate()

            } else {
                Log.e("LoginActivity", "❌ Token no disponible después del tiempo límite")
                Toast.makeText(
                    this@LoginActivity,
                    "No se pudo obtener el token. Verifica conexión.",
                    Toast.LENGTH_LONG
                ).show()
                employeeViewModel.onCreate()
            }
        }


        initViewModel.initState.observe(this) { state ->
            when (state) {
                is InitState.Loading -> {
                    // Opcional: Desactiva UI o muestra loader
                    loginBtn.isEnabled = false
                    Toast.makeText(this, "Cargando datos iniciales...", Toast.LENGTH_SHORT).show()
                }

                is InitState.Success -> {
                    // Activar UI cuando la carga termine
                    loginBtn.isEnabled = true
                    Toast.makeText(this, "Datos cargados correctamente", Toast.LENGTH_SHORT).show()
                }

                is InitState.Error -> {
                    Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                    Log.e("InitViewModel", "Error al cargar datos iniciales: ${state.message}")
                    loginBtn.isEnabled = false
                }
            }
        }

        // Referencias UI
        autoCompleteEmpleado = findViewById(R.id.autoCompleteEmpleado)
        numeroEdit = findViewById(R.id.editnumber)
        loginBtn = findViewById(R.id.btnIniciar)


        workShiftViewModel.obtenerTurnoActual()

        processWorkOrdersViewModel.resultado.observe(this) { result ->
            result.onSuccess {
                Log.d("Proceso", "Órdenes procesadas correctamente")
            }.onFailure {
                Log.e("Proceso", "Error al procesar órdenes: ${it.message}")
                Toast.makeText(this, "Error al guardar las órdenes", Toast.LENGTH_LONG).show()
            }
        }


        workOrderViewModel.ordenesFiltradas.observe(this) { ordenes ->
            Log.d("OrdenesFiltradas", "Se obtuvieron: ${ordenes.size} órdenes para el empleado")

            ordenes.forEach { orden ->
                Log.d(
                    "Orden",
                    "Orden ID: ${orden.workOrderId}, Número: ${orden.workOrderNumber}, Fecha: ${orden.createdAt}"
                )

                orden.placeWorkOrders.forEach { place ->
                    Log.d(
                        "Lugar",
                        "Lugar ID: ${place.placeId}, Tareas: ${place.taskPlannings.size}"
                    )

                    place.taskPlannings.forEach { tarea ->
                        Log.d(
                            "Tarea",
                            "Tarea ID: ${tarea.taskPlanningId}, Vehículo: ${tarea.indexVehicleId}, Empleado: ${tarea.taskAssignment.indexEmployee.dispatchEmployeeId}, Cantidad: ${tarea.quantity}"
                        )
                    }
                }
            }
        }

        workShiftViewModel.turnoActual.observe(this) { turno ->
            turno?.let {
                Log.d("TurnoActual", "Turno actual: ${it.description}, ID: ${it.id}")
            } ?: Log.d("TurnoActual", "No se encontró un turno actual")
        }


        fun String.normalize(): String {
            return Normalizer.normalize(this, Normalizer.Form.NFD)
                .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
                .lowercase()
        }

        employeeViewModel.employeeList.observe(this) { empleados ->
            empleados?.let {
                val nombresEmpleados = empleados.map { "${it.Name} ${it.PaternalLastName} ${it.MaternalLastName}" }

                // Copia inmutable para siempre tener la lista original
                val nombresOriginales = nombresEmpleados.toList()

                val adapter = object : ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    nombresEmpleados.toMutableList()
                ) {
                    override fun getFilter(): Filter {
                        return object : Filter() {
                            override fun performFiltering(constraint: CharSequence?): FilterResults {
                                val results = FilterResults()
                                val filtered = if (!constraint.isNullOrBlank()) {
                                    val normalizedConstraint = constraint.toString().normalize()
                                    nombresOriginales.filter {
                                        it.normalize().contains(normalizedConstraint)
                                    }
                                } else {
                                    // Mostrar solo los primeros 4 cuando no se ha escrito nada
                                    nombresOriginales.take(4)
                                }
                                results.values = filtered
                                results.count = filtered.size
                                return results
                            }

                            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                                clear()
                                if (results?.values != null) {
                                    @Suppress("UNCHECKED_CAST")
                                    addAll(results.values as List<String>)
                                }
                                notifyDataSetChanged()
                            }
                        }
                    }
                }

                autoCompleteEmpleado.setAdapter(adapter)
                autoCompleteEmpleado.threshold = 1

                autoCompleteEmpleado.setOnItemClickListener { _, _, position, _ ->
                    val seleccionado = adapter.getItem(position)
                    empleadoSeleccionado = empleados.firstOrNull {
                        "${it.Name} ${it.PaternalLastName} ${it.MaternalLastName}" == seleccionado
                    }
                }


                autoCompleteEmpleado.setAdapter(adapter)
                autoCompleteEmpleado.threshold = 1 // empieza a sugerir desde el primer carácter

                // Detectar la selección desde la lista desplegable
                autoCompleteEmpleado.setOnItemClickListener { _, _, position, _ ->
                    val seleccionadoNombre = adapter.getItem(position)
                    empleadoSeleccionado = empleados.firstOrNull {
                        "${it.Name} ${it.PaternalLastName} ${it.MaternalLastName}" == seleccionadoNombre
                    }

                    Log.d("EmpleadoSeleccionado", "Seleccionado: ${empleadoSeleccionado?.DispatchEmployeeId}")
                }
            }
        }

        // Login botón
        loginBtn.setOnClickListener {
            val nombre = autoCompleteEmpleado.text.toString()
            val numero = numeroEdit.text.toString()

            if (empleadoSeleccionado == null) {
                Toast.makeText(this, "Selecciona un empleado válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(nombre, numero)
        }

        // Observa login exitoso
        lifecycleScope.launchWhenStarted {
            viewModel.loginSuccess.collect { success ->
                if (success) {
                    Toast.makeText(this@LoginActivity, "Inicio Exitoso", Toast.LENGTH_SHORT).show()

                    empleadoSeleccionado?.let { empleado ->

                        sessionManager.saveIndexEmployeeId(empleado.IndexEmployeeId)

                        Log.d("Login", "Empleado seleccionado: ${empleado.DispatchEmployeeId}")
//                        workOrderViewModel.obtenerOrdenesFiltradas(empleado.DispatchEmployeeId)

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("dispatchEmployeeId", empleado.DispatchEmployeeId)
                        intent.putExtra("indexEmployeeId", empleado.IndexEmployeeId)

                        delay(500)

                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        // Observa errores de login
        lifecycleScope.launchWhenStarted {
            viewModel.error.collect { errorMsg ->
                errorMsg?.let {
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    /* Función que define la pantalla como FULLSCREEN, oculta la
    barra de actividades y de acción (atras, inicio, recientes) */

    private fun esconderBarras() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }
}
