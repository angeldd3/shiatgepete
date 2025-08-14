package com.lasec.monitoreoapp.presentation.screens.actividad_sinorden

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lasec.monitoreoapp.R
import com.lasec.monitoreoapp.core.util.SessionManager
import com.lasec.monitoreoapp.domain.extensions.generateSlotsEvery30Min
import com.lasec.monitoreoapp.presentation.screens.checklist.ChecklistActivity
import com.lasec.monitoreoapp.presentation.screens.home.HomeActivity
import com.lasec.monitoreoapp.presentation.screens.login.LoginActivity
import com.lasec.monitoreoapp.domain.extensions.generateFinalTimeSlotsEvery30Min
import com.lasec.monitoreoapp.domain.extensions.isTimeFormat
import com.lasec.monitoreoapp.domain.extensions.calcQuantity
import com.lasec.monitoreoapp.domain.extensions.isTimeFormat
import com.lasec.monitoreoapp.domain.extensions.calcQuantity
import com.lasec.monitoreoapp.domain.extensions.endTimeFromQuantityExact
import com.lasec.monitoreoapp.domain.extensions.generateFinalTimeSlotsEvery30Min
import com.lasec.monitoreoapp.domain.extensions.manual_workorders.extractHMS
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SinOrderActivity : AppCompatActivity() {

    @Inject lateinit var sessionManager: SessionManager

    // --- Helpers ---
    private fun isTime(text: String) = text.matches(Regex("\\d{2}:\\d{2}(:\\d{2})?"))

    // Variable para la lógica de hora de inicio de actividad
    // Hora inicio / final y cantidad
    lateinit var editHora: EditText
    lateinit var editHoraFinal: EditText
    lateinit var editCantidad: EditText

    // Variables para el contador de tiempo trans.
    private lateinit var btnIniciar: ImageButton
    private lateinit var tvTiempo: TextView
    private var contador = 0
    private var handlers = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var contando = false

    // Iconos de wifi y bluetooth
    private lateinit var ic_wifi: ImageView
    private lateinit var ic_bt: ImageView

    // Barra de progreso
    private lateinit var tvProgreso: TextView
    private lateinit var progreso: ProgressBar
    private lateinit var avanzar: Button
    private lateinit var reducir: Button
    private var progresoActual = 0

    // Reloj
    private lateinit var tvReloj: TextView

    private val manualWorkOrdersViewModel: ManualWorkOrdersViewModel by viewModels()
    private val registerVM: ManualWorkOrdersRegisterViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())

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
        setContentView(R.layout.activity_sin_order)
        supportActionBar?.hide()
        esconderBarras()

        initViews()
        observeViewModel()
        setupListeners()

        // Carga inicial
        val indexEmployeeId = sessionManager.getIndexEmployeeId()
        Log.d("SinOrderActivity", "indexEmployeeId recuperado: $indexEmployeeId")
        manualWorkOrdersViewModel.loadVehicles(indexEmployeeId)
        manualWorkOrdersViewModel.loadUnauthorizedPlaces()
        manualWorkOrdersViewModel.loadCurrentShift()

        handler.post(relojRunnable)
        actualizarWifi()
        actualizarBluetooth()
    }

    // -------------------- initViews --------------------
    private fun initViews() {
        tvReloj = findViewById(R.id.tv_reloj)
        editHora = findViewById(R.id.sp_horaInicio)
        editHora.isEnabled = false

        editHoraFinal = findViewById(R.id.sp_horaFinal)
        editHoraFinal.isFocusable = false
        editHoraFinal.isClickable = true

        editCantidad = findViewById(R.id.sp_cantidadRealizar)

        ic_wifi = findViewById(R.id.ic_wifich)
        ic_bt = findViewById(R.id.ic_bluetoothch)
    }

    // -------------------- observeViewModel --------------------
    private fun observeViewModel() {
        manualWorkOrdersViewModel.currentShift.observe(this) { shift ->
            if (shift != null) {
                Log.d("SinOrderActivity", "Turno actual: ${shift.description}")
                editHora.isEnabled = true
            } else {
                Log.d("SinOrderActivity", "No hay turno actual")
                editHora.isEnabled = false
            }
        }

        // Spinners
        val noEconomico: Spinner = findViewById(R.id.sp_noEconomico)
        val tipoActividad: Spinner = findViewById(R.id.sp_tipoActividad)
        val sitioActividad: Spinner = findViewById(R.id.sp_sitioActividad)

        manualWorkOrdersViewModel.vehicles.observe(this) { vehicleList ->
            val nombres = vehicleList.map { it.economic_number }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            noEconomico.adapter = adapter
        }

        manualWorkOrdersViewModel.activities.observe(this) { activityList ->
            val nombres = activityList.map { it.activityName }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tipoActividad.adapter = adapter
        }

        manualWorkOrdersViewModel.unauthorizedPlaces.observe(this) { placesList ->
            val nombres = placesList.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombres)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sitioActividad.adapter = adapter
        }
    }

    // -------------------- setupListeners --------------------
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListeners() {
        // Botón perfil → popup logout
        val btnCerrarSesion = findViewById<Button>(R.id.btn_userProfile)
        btnCerrarSesion.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.cu_menu_logout, null)
            val popupWindow = PopupWindow(
                view,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
            )
            popupWindow.showAsDropDown(btnCerrarSesion)
            view.findViewById<TextView>(R.id.option_logout).setOnClickListener {
                popupWindow.dismiss()
                cerrarSesion()
            }
        }

        // Navegación
        findViewById<ImageButton>(R.id.btn_sinorder).setOnClickListener {
            if (this !is SinOrderActivity) {
                startActivity(Intent(this, SinOrderActivity::class.java))
                finish()
            }
        }
        findViewById<ImageButton>(R.id.btn_checklist).setOnClickListener {
            startActivity(Intent(this, ChecklistActivity::class.java))
        }
        findViewById<ImageButton>(R.id.btn_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Selector de hora de inicio (slots de 30 min del turno)
        editHora.setOnClickListener {
            val shift = manualWorkOrdersViewModel.currentShift.value
            if (shift != null) {
                val opciones = shift.generateSlotsEvery30Min()
                AlertDialog.Builder(this)
                    .setTitle("Selecciona hora de inicio")
                    .setItems(opciones.toTypedArray()) { d, i ->
                        editHora.setText(opciones[i])
                        d.dismiss()

                        // Si ya hay hora final válida, recalcula cantidad
                        val finActual = editHoraFinal.text?.toString()?.trim().orEmpty()
                        if (isTimeFormat(finActual)) {
                            val qty = calcQuantity(opciones[i], finActual)
                            editCantidad.setText(
                                if (qty % 1f == 0f) qty.toInt().toString()
                                else String.format(Locale.US, "%.1f", qty)
                            )
                        }
                    }
                    .show()
            } else {
                Toast.makeText(this, "No hay turno actual cargado.", Toast.LENGTH_SHORT).show()
            }
        }

        editHoraFinal.setOnClickListener {
            val shift = manualWorkOrdersViewModel.currentShift.value
            val inicio = editHora.text?.toString()?.trim().orEmpty()

            if (shift == null) {
                Toast.makeText(this, "No hay turno actual cargado.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isTimeFormat(inicio)) {
                Toast.makeText(this, "Selecciona primero la hora de inicio.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val opcionesFin = generateFinalTimeSlotsEvery30Min(shift, inicio)
            if (opcionesFin.isEmpty()) {
                Toast.makeText(this, "No hay opciones de hora final dentro del turno.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Selecciona hora final")
                .setItems(opcionesFin.toTypedArray()) { d, i ->
                    val finElegida = opcionesFin[i]
                    editHoraFinal.setText(finElegida)

                    // Cantidad automática
                    val qty = calcQuantity(inicio, finElegida)
                    editCantidad.setText(
                        if (qty % 1f == 0f) qty.toInt().toString()
                        else String.format(Locale.US, "%.1f", qty)
                    )
                    d.dismiss()
                }
                .show()
        }





        // Spinners dependientes
        val noEconomico: Spinner = findViewById(R.id.sp_noEconomico)
        noEconomico.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedVehicle = manualWorkOrdersViewModel.vehicles.value?.get(position)
                selectedVehicle?.let { manualWorkOrdersViewModel.loadActivities(it.indexVehicleId) }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        editCantidad.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(s: android.text.Editable?) {
                val shift = manualWorkOrdersViewModel.currentShift.value ?: return
                val inicio = editHora.text?.toString()?.trim().orEmpty()
                if (!isTimeFormat(inicio)) return

                val qty = s?.toString()?.trim()?.replace(',', '.')?.toFloatOrNull() ?: return
                val fin = endTimeFromQuantityExact(shift, inicio, qty)
                if (fin == null) {
                    editHoraFinal.text?.clear()
                    Toast.makeText(this@SinOrderActivity, "La cantidad excede el fin del turno.", Toast.LENGTH_SHORT).show()
                } else if (editHoraFinal.text?.toString() != fin) {
                    editHoraFinal.setText(fin) // HH:mm:01 exacto, sin redondeo a 30
                }
            }
        })


        // Botón "Calcular Hora Final" (tu lógica original)
//        val editMinutosExtra = findViewById<EditText>(R.id.sp_cantidadRealizar)
//        val editResultadoHora = findViewById<EditText>(R.id.sp_horaFinal)
//        val btnSumarTiempo = findViewById<Button>(R.id.btn_calcularHoraFinal)
//
//        btnSumarTiempo.setOnClickListener {
//            val horaStr = editHora.text.toString()
//            val minutosExtraStr = editMinutosExtra.text.toString()
//
//            if (horaStr.contains(":") && minutosExtraStr.isNotEmpty()) {
//                val partes = horaStr.split(":")
//                val horaBase = partes[0].toIntOrNull() ?: 0
//                val minutosBase = partes[1].toIntOrNull() ?: 0
//                val minutosExtra = minutosExtraStr.toIntOrNull() ?: 0
//
//                if (minutosExtra > 100) {
//                    Toast.makeText(this, "El valor no puede ser mayor a 100", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//
//                val totalMinutos = horaBase * 60 + minutosBase + (minutosExtra * 5)
//                val nuevaHora = (totalMinutos / 60) % 24
//                val nuevosMinutos = totalMinutos % 60
//
//                val horaFormateada = String.format("%02d:%02d", nuevaHora, nuevosMinutos)
//                editResultadoHora.setText(horaFormateada)
//            } else {
//                Toast.makeText(this, "Completa los campos correctamente", Toast.LENGTH_SHORT).show()
//            }
//        }

        // Enviar formulario (igual que tenías)
        val btnEnviarFormulario = findViewById<Button>(R.id.btn_enviarForm)
        btnEnviarFormulario.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_sinorder, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).create()
            val btnAceptar = dialogView.findViewById<Button>(R.id.btn_aceptar)
            val btnCancelar = dialogView.findViewById<Button>(R.id.btn_cancelar)
            btnCancelar.setOnClickListener { dialog.dismiss() }
            btnAceptar.setOnClickListener {
                val shift = manualWorkOrdersViewModel.currentShift.value
                if (shift == null) {
                    Toast.makeText(this, "No hay turno actual cargado", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val inicio = editHora.text.toString().trim()
                val fin    = editHoraFinal.text.toString().trim()
                val cantidad = editCantidad.text.toString().toDoubleOrNull() ?: 0.0

                if (!isTimeFormat(inicio)) {
                    Toast.makeText(this, "Selecciona la hora de inicio.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!isTimeFormat(fin)) {
                    Toast.makeText(this, "Selecciona la hora final.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val vehicle = manualWorkOrdersViewModel.vehicles.value?.getOrNull(
                    findViewById<Spinner>(R.id.sp_noEconomico).selectedItemPosition
                )
                val activity = manualWorkOrdersViewModel.activities.value?.getOrNull(
                    findViewById<Spinner>(R.id.sp_tipoActividad).selectedItemPosition
                )
                val place = manualWorkOrdersViewModel.unauthorizedPlaces.value?.getOrNull(
                    findViewById<Spinner>(R.id.sp_sitioActividad).selectedItemPosition
                )

                val today = java.time.LocalDate.now()
                val initIso = "${today}T${if (inicio.length == 5) "$inicio:00" else inicio}"
                val endIso  = "${today}T${if (fin.length == 5) "$fin:00" else fin}"

                // Usar la extensión para obtener horas/min/seg
                val hms = shift.extractHMS()


                registerVM.registerActivity(
                    indexEmployeeId = sessionManager.getIndexEmployeeId(),

                    workShiftId = shift.id,
                    workShiftDescription = shift.description,
                    workShiftStart = shift.startTime,   // <- String "HH:mm[:ss]"
                    workShiftEnd   = shift.endTime,     // <- String "HH:mm[:ss]"

                    indexVehicleId = vehicle?.indexVehicleId ?: 0,
                    economicNumber = vehicle?.economic_number.orEmpty(),
                    vehicleTypeId  = vehicle?.vehicleTypeId ?: 0,

                    activityTypeId = activity?.activityTypeId ?: 0,
                    activityName   = activity?.activityName.orEmpty(),

                    quantity = cantidad,

                    placeId   = place?.id ?: 0,        // <- si tu PlacesEntity usa 'id'
                    placeName = place?.name.orEmpty(),

                    initTimeIso = initIso,
                    endTimeIso  = endIso
                )

                dialog.dismiss()
            }

            dialog.show()
            dialog.window?.setDimAmount(0.2f)
        }
    }

    // -------------------- Red / Estado --------------------
    private fun actualizarWifi() {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

        val wifiEncendido = wifiManager.isWifiEnabled
        val conectadoWifi = networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected

        if (wifiEncendido) {
            if (conectadoWifi) ic_wifi.setImageResource(R.drawable.ic_wifi_on)
            else ic_wifi.setImageResource(R.drawable.ic_wifi_enabled)
        } else {
            ic_wifi.setImageResource(R.drawable.ic_wifi_off)
        }
    }

    private fun actualizarBluetooth() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            ic_bt.setImageResource(R.drawable.ic_bluetooth_off)
        } else {
            if (bluetoothAdapter.isEnabled) ic_bt.setImageResource(R.drawable.ic_bluetooth_on)
            else ic_bt.setImageResource(R.drawable.ic_bluetooth_off)
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
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(estadoReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(relojRunnable)
    }

    private fun esconderBarras() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }

    // ---- Tu TimePicker original lo dejo intacto por si lo usas en otro lado ----
    private fun mostrarHora() {
        val calendario = Calendar.getInstance()
        val hora = calendario.get(Calendar.HOUR_OF_DAY)
        val minuto = calendario.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, selectedHour)
                cal.set(Calendar.MINUTE, selectedMinute)
                val formato = SimpleDateFormat("HH:mm", Locale.getDefault())
                val horaFormateada = formato.format(cal.time)
                editHora.setText(horaFormateada)
            },
            hora, minuto, true
        )
        timePicker.show()
    }

    private fun cerrarSesion() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
