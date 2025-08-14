package com.lasec.monitoreoapp.presentation.screens.home

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PorterDuff
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lasec.monitoreoapp.R
import com.lasec.monitoreoapp.R.color.orange_orange_500
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntity
import com.lasec.monitoreoapp.domain.model.TaskFullDetail
import com.lasec.monitoreoapp.presentation.screens.actividad_sinorden.SinOrderActivity
import com.lasec.monitoreoapp.presentation.screens.checklist.ChecklistActivity
import com.lasec.monitoreoapp.presentation.screens.login.LoginActivity
import com.lasec.monitoreoapp.presentation.screens.login.ProcessWorkOrdersViewModel
import com.lasec.monitoreoapp.presentation.viewmodel.ProgressLogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), SensorEventListener {

    //Variable para definir los viewmodels utilizados para la logica de la pantalla
    private val processWorkOrdersViewModel: ProcessWorkOrdersViewModel by viewModels()
    private val tasksViewModel: TasksViewModel by viewModels()
    private val progressLogViewModel: ProgressLogViewModel by viewModels()

    //Variables para el uso del acelerometro
    private lateinit var sensorManager: SensorManager
    private var acelerometro: Sensor? = null
    private lateinit var tv_Estado: TextView

    //Variables para la logica del contador de tiempo transcurrido
    private lateinit var btnIniciar: ImageButton
    private lateinit var btnReiniciar: ImageButton
    private lateinit var btnDetener: ImageButton
    private lateinit var tvTiempo: TextView
    private var contador = 0
    private var handlers = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var isRunning = false
    private var contando = false

    //Variables para los iconos de wifi y bluetooth
    private lateinit var ic_wifi: ImageView
    private lateinit var ic_bt: ImageView

    //Variables para la barra de progreso
    private lateinit var tvConteo: TextView
    private lateinit var tvProgreso: TextView
    private lateinit var progreso: ProgressBar
    private lateinit var avanzar: Button
    private lateinit var reducir: Button
    private var totalCantidad: Float = 0f
    private var progresoActual = 0
    private var taskId = 0

    //Variable para el contador de hora final
    private lateinit var tvHoraFinal : TextView

    //Logica para la fecha y hora
    private lateinit var tvReloj: TextView
    private val handler = Handler(Looper.getMainLooper())

    //Variable para definir el RecyclerView de las activiades
    private lateinit var rvActividades: RecyclerView

    private val relojRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            val ahora = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy\nHH:mm:ss")
            tvReloj.text = ahora.format(formatter)
            handler.postDelayed(this, 1000)
        }
    }

    private val estadoReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                WifiManager.WIFI_STATE_CHANGED_ACTION,
                ConnectivityManager.CONNECTIVITY_ACTION -> actualizarWifi()
                BluetoothAdapter.ACTION_STATE_CHANGED -> actualizarBluetooth()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        tvHoraFinal = findViewById(R.id.tv_horaFinal)

        supportActionBar?.hide()
        esconderBarras()

        /*  Logica para la obtencion de actividades si es que se le asigno una orden de trabajo
        *  ordenando por hora y realizando la logica tanto de impresión de datos en la pantalla
        *  como de logica de avances de la tarea */

        val dispatchEmployeeId = intent.getIntExtra("dispatchEmployeeId", -1)
        if (dispatchEmployeeId != -1) {
            Log.d("HomeActivity", "Procesando órdenes para empleado ID: $dispatchEmployeeId")

            processWorkOrdersViewModel.procesarOrdenesParaEmpleado(dispatchEmployeeId)

            processWorkOrdersViewModel.ordenesProcesadas.observe(this) { terminado ->
                if (terminado == true) {
                    Log.d("HomeActivity", "✅ Procesamiento terminado, cargando tareas")
                    tasksViewModel.loadTaskDetails(dispatchEmployeeId)
                }
            }
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_actividadesasignadas, null)
        val btnRechazar = dialogView.findViewById<Button>(R.id.btn_cancelar)
        val btnAceptar = dialogView.findViewById<Button>(R.id.btn_aceptar)

        val avisoSinOrden = findViewById<TextView>(R.id.tv_aviso_sin_orden)
        avisoSinOrden.visibility = View.VISIBLE

        val btnCrearActividad = findViewById<AppCompatButton>(R.id.btn_crearActividad)
        btnCrearActividad.visibility = View.VISIBLE

        val barraDatosOrden = findViewById<ConstraintLayout>(R.id.cl_barraDatosOrden)
        barraDatosOrden.visibility = View.GONE

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Escuchar los detalles de tareas
        tasksViewModel.taskDetails.observe(this) { taskList ->
            if (!taskList.isNullOrEmpty()) {
                Log.d("TaskDetails", "Tareas cargadas: $taskList")

                val dialogView = layoutInflater.inflate(R.layout.dialog_actividadesasignadas, null)
                val btnRechazar = dialogView.findViewById<Button>(R.id.btn_cancelar)
                val btnAceptar = dialogView.findViewById<Button>(R.id.btn_aceptar)

                val dialog = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create()

                btnRechazar.setOnClickListener {
                    dialog.dismiss()
                }

                btnAceptar.setOnClickListener {
                    //Ordenar actividades por fecha de inicio
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    val tareasOrdenadas = taskList.sortedBy {
                        LocalDateTime.parse(it.initTime, formatter)
                    }

                    // Mostrar actividades enlistadas en un Recycler View
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerTasks)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = TaskAdapter(tareasOrdenadas)
                    val primerTask = tareasOrdenadas[0]
                    val parsedTime = LocalDateTime.parse(primerTask.endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    val zonedTime = parsedTime.atZone(ZoneId.of("UTC"))
                    val localTime = zonedTime.withZoneSameInstant(ZoneId.systemDefault())
                    val formatters = DateTimeFormatter.ofPattern("hh:mm")
                    val formatted = localTime.format(formatters)

                    //Agregar los valores de numero de orden, y horafinal formateada
                    findViewById<TextView>(R.id.tv_orderNumber).text =
                        getString(R.string.order_number, primerTask.workOrderNumber)

                    findViewById<TextView>(R.id.tv_horaFinal).text =
                        getString(R.string.estimated_end_time, formatted)



                    val porcentaje = primerTask.progressPercentage ?: 0.0
                    val cantidadActual = primerTask.progressQuantity ?: 0.0

                    //Logica para el conteo de barrenos
                    totalCantidad = primerTask.quantity.toFloat()
                    progresoActual = cantidadActual.toInt()
                    val format = DecimalFormat("#.##")
                    progreso.max = totalCantidad.toInt()
                    progreso.progress = progresoActual
                    tvConteo.text = getString(R.string.completado_text, format.format(cantidadActual), format.format(totalCantidad))
                    tvProgreso.text = getString(R.string.progreso_text, format.format(porcentaje))


                    //Setear valor a la variable de taskId
                    taskId = primerTask.taskPlanningId

                    // Ocultar aviso y mostrar barra de orden
                    findViewById<TextView>(R.id.tv_aviso_sin_orden).visibility = View.GONE
                    findViewById<AppCompatButton>(R.id.btn_crearActividad).visibility = View.GONE
                    findViewById<ConstraintLayout>(R.id.cl_barraDatosOrden).visibility =
                        View.VISIBLE

                    findViewById<ImageButton>(R.id.btn_pausa).setBackgroundResource(R.drawable.ic_start_activity_green)
                    findViewById<ImageButton>(R.id.btn_cancelar).setBackgroundResource(R.drawable.ic_cancel_activity_red)

                    dialog.dismiss()
                }

                // Retraso opcional antes de mostrar
                lifecycleScope.launch {
                    delay(2000L)
                    dialog.show()
                }
            } else {
                // Si no hay tareas, se mantiene visible el aviso
                findViewById<TextView>(R.id.tv_aviso_sin_orden).visibility = View.VISIBLE
                findViewById<AppCompatButton>(R.id.btn_crearActividad).visibility = View.VISIBLE
                findViewById<ConstraintLayout>(R.id.cl_barraDatosOrden).visibility = View.GONE
            }
        }

        //Logica para el estado de la actividad
            tv_Estado = findViewById(R.id.ic_estado)

            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

            if (acelerometro == null){
                tv_Estado.text = "No Disponible"
            }
        //Termina logica de estado de actividad

        //Logica para el estado de la actividad
        val tvSitio = findViewById<TextView>(R.id.tv_sitios)
        val sitio_progreso = 25

        if (sitio_progreso == 50) {
            tvSitio.text = "REB.67,41 W"
        } else if (sitio_progreso == 25) {
            tvSitio.text = "F. 150 W"
        } else if (sitio_progreso == 10) {
            tvSitio.text = "R. 24, 45W"
        }
        //Termina logica de estado de actividad

        //Variable para la referencia del boton
            val btnCerrarSesion = findViewById<Button>(R.id.btn_userProfile)

            btnCerrarSesion.setOnClickListener {
                val inflater = layoutInflater
                val view = inflater.inflate(R.layout.cu_menu_logout, null)

                val popupWindow = PopupWindow(view,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true)

                // Mostrar debajo del botón
                popupWindow.showAsDropDown(btnCerrarSesion)

                // Acción del botón dentro del popup
                val logoutOption = view.findViewById<TextView>(R.id.option_logout)
                logoutOption.setOnClickListener {
                    popupWindow.dismiss()
                    cerrarSesion()
                }
            }

        /* Lógica que implementa la funcionalidad de los diversos botones
        de acción para las actividades que se realizaran*/

        tvTiempo = findViewById(R.id.tv_tiempotrans)

        btnReiniciar = findViewById(R.id.btn_detener)
        btnReiniciar.visibility = View.GONE

        btnIniciar = findViewById(R.id.btn_pausa)
        btnIniciar.visibility = View.VISIBLE

        btnDetener = findViewById(R.id.btn_cancelar)

        btnIniciar.setOnClickListener{
            if (!contando){
                iniciarContador()
                btnIniciar.visibility = View.GONE
                btnReiniciar.visibility = View.VISIBLE
            }
        }

        btnReiniciar.setOnClickListener{
            pausarContador()
            btnIniciar.visibility = View.VISIBLE
            btnReiniciar.visibility = View.GONE
            tv_Estado.text = "Pausado"
            tv_Estado.setTextColor(ContextCompat.getColor(this, orange_orange_500))
        }

        /* Lógica para la reedirección de el aviso a la hora de que el usuario quiere cancelar
        * la activiad que se esta realizando, teniendo que levantar un reporte de incidencia
        * en la pantalla correspondiente */

        btnDetener.setOnClickListener{
            val dialogView = layoutInflater.inflate(R.layout.dialog_personalizado, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).create()

            val titulo = dialogView.findViewById<TextView>(R.id.dialog_titulo)
            titulo.text = "¿Estas seguro de que deseas finalizar la actividad?"

            val mensaje = dialogView.findViewById<TextView>(R.id.dialog_mensaje)
            mensaje.text = "Tendrás que levantar un reporte explicando el mótivo de la finalización de la actividad"

            val btnAceptar = dialogView.findViewById<Button>(R.id.btn_aceptar)
            val btnCancelar = dialogView.findViewById<Button>(R.id.btn_cancelar)

            btnCancelar.setOnClickListener { dialog.dismiss() }
            btnAceptar.setOnClickListener {
                startActivity(Intent(this, ChecklistActivity::class.java))
                dialog.dismiss()
            }
            dialog.show()
            dialog.window?.setDimAmount(0.2f)
        }

        // Iconos de estado de red
        ic_wifi = findViewById(R.id.ic_wifich)
        ic_bt = findViewById(R.id.ic_bluetoothch)

        actualizarWifi()
        actualizarBluetooth()

        // Dialogo crear actividad sin orden
        val botonMostrarDialogo = findViewById<Button>(R.id.btn_crearActividad)
        botonMostrarDialogo.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_personalizado, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).create()

            val btnAceptar = dialogView.findViewById<Button>(R.id.btn_aceptar)
            val btnCancelar = dialogView.findViewById<Button>(R.id.btn_cancelar)

            btnCancelar.setOnClickListener { dialog.dismiss() }
            btnAceptar.setOnClickListener {
                startActivity(Intent(this, SinOrderActivity::class.java))
                dialog.dismiss()
            }
            dialog.show()
            dialog.window?.setDimAmount(0.2f)
        }

        // Botones navegación
        findViewById<ImageButton>(R.id.btn_home).setOnClickListener {
            if (this !is HomeActivity) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        findViewById<ImageButton>(R.id.btn_checklist).setOnClickListener {
            startActivity(Intent(this, ChecklistActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btn_sinorder).setOnClickListener {
            startActivity(Intent(this, SinOrderActivity::class.java))
        }

        // Visor 3D
        val webView = findViewById<WebView>(R.id.int_visor3d)
        with(webView) {
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            webChromeClient = WebChromeClient()
            loadUrl("file:///android_asset/visor3D.html")
        }

        // Reloj y progreso
        tvReloj = findViewById(R.id.tv_reloj)
        tvProgreso = findViewById(R.id.tv_progreso)
        tvConteo = findViewById(R.id.tv_Conteo)
        handler.post(relojRunnable)

        progreso = findViewById(R.id.barra_progreso)
        avanzar = findViewById(R.id.btn_arriba)
        reducir = findViewById(R.id.btn_abajo)
        Log.e("Progreso", "Valor del progreso actual:${progresoActual}")

        avanzar.setOnClickListener {
            if (progresoActual < totalCantidad) {
                progresoActual++
                progreso.progress = progresoActual

                val format = DecimalFormat("#.##")

                val porcentaje = (progresoActual * 100) / totalCantidad
                tvProgreso.text = "${format.format(porcentaje)}% Completado"


                val tvConteo = findViewById<TextView>(R.id.tv_Conteo)
                tvConteo.text = "${format.format(progresoActual.toDouble())}/${format.format(totalCantidad)} Completado"

                if (progresoActual.toFloat() == totalCantidad) {
                    val dialogView = layoutInflater.inflate(R.layout.dialog_finalizacionact, null)
                    val dialog = AlertDialog.Builder(this).setView(dialogView).create()
                    dialogView.findViewById<Button>(R.id.btn_aceptar).setOnClickListener {
                        startActivity(Intent(this, HomeActivity::class.java))
                        dialog.dismiss()
                    }
                    dialog.show()
                    dialog.window?.setDimAmount(0.2f)
                }

                val log = ProgressLogEntity(
                    id = 0,
                    quantity = progresoActual.toDouble(),
                    percentage = porcentaje.toDouble(),
                    taskPlanningId = taskId,
                    activityStatusId = 1
                )
                progressLogViewModel.insertarYSincronizarLog(log)
            }
        }

        reducir.setOnClickListener {
            if (progresoActual > 0) {
                progresoActual--
                progreso.progress = progresoActual
                val format = DecimalFormat("#.##")
                val porcentaje = (progresoActual * 100) / totalCantidad
                tvProgreso.text = "${format.format(porcentaje)}% Completado"


                val tvConteo = findViewById<TextView>(R.id.tv_Conteo)
                tvConteo.text = "${format.format(progresoActual.toDouble())}/${format.format(totalCantidad)} Completado"


                Log.e("PROGRESS", "Valor del Progreso: $progresoActual")
                val log = ProgressLogEntity(
                    id = 0,
                    quantity = progresoActual.toDouble(),
                    taskPlanningId = taskId,
                    percentage = porcentaje.toDouble(),
                    activityStatusId = 1
                )
                progressLogViewModel.insertarYSincronizarLog(log)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filtro = IntentFilter().apply {
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }
        registerReceiver(estadoReceiver, filtro)
        acelerometro?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(estadoReceiver)
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(relojRunnable)
    }

    /* Función que permite la lectura del acelerometro, permitiendo definir un rango de datos para
     el estado de la actividad definicneod si esta activo = barrenando o si esta detenido */
    var lastUpdate = 0L
    override  fun onSensorChanged(event: SensorEvent?){
        val currentTime = System.currentTimeMillis()

        if (currentTime - lastUpdate >= 2000){
            lastUpdate = currentTime

            val x = event?.values?.get(0) ?: 0f
            val y = event?.values?.get(1) ?: 0f
            val z = event?.values?.get(2) ?: 0f

            val magnitude = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            if (magnitude <= 10f) {
                tv_Estado.setTextColor(ContextCompat.getColor(this, R.color.red))
                tv_Estado.text = "Detenido"
            }else if(magnitude >= 12f){
                tv_Estado.text = "Barrenando"
                tv_Estado.setTextColor(ContextCompat.getColor(this, R.color.green))
            }
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int){}

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

    //Función que le permite al usuario iniciar el contador de tiempo transcurrido
    private fun iniciarContador() {
        if (!isRunning) {
            isRunning = true
            runnable = object : Runnable {
                override fun run() {
                    if (isRunning) {
                        contador++
                        val horas = contador / 3600
                        val minutos = (contador % 3600) / 60
                        val segundos = contador % 60

                        val formatTime = String.format("%02d:%02d:%02d", horas, minutos, segundos)
                        tvTiempo.text = "Tiempo Transcurrido: $formatTime"

                        handlers.postDelayed(this, 1000)
                    }
                }
            }
            handlers.post(runnable)
        }
    }
    //Función que permite al usuario pausar el contador de tiempo transcurrido
    private fun pausarContador() {
        isRunning = false
        handlers.removeCallbacks(runnable)
    }

    //Función que permite el cierre de sesión al usuario
    private fun cerrarSesion(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    /* Función que permite la lectura del estado de la señal wifi, podiendo mostrarla con un icono
    * según el estado del wifi */
    private fun actualizarWifi() {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

        val wifiEncendido = wifiManager.isWifiEnabled
        val conectadoWifi = networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected

        if (wifiEncendido) {
            if (conectadoWifi) {
                ic_wifi.setImageResource(R.drawable.ic_wifi_on) //Cuando el wifi esta encendido y conectado a la red
            } else {
                ic_wifi.setImageResource(R.drawable.ic_wifi_enabled) //Cuando el wifi esta encendido y pero no conectado a la red
            }
        } else {
            ic_wifi.setImageResource(R.drawable.ic_wifi_off) //Cuando el wifi esta apagado
        }
    }

    /* Función que permite la lectura del estado del bluetooth, podiendo mostrar con un icono
    * según el estado del bluetooth */
    private fun actualizarBluetooth() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            ic_bt.setImageResource(R.drawable.ic_bluetooth_off) //Cuando esta apagado
        } else {
            if (bluetoothAdapter.isEnabled) {
                ic_bt.setImageResource(R.drawable.ic_bluetooth_on) //Cuando esta encendido
            }
        }
    }

}
