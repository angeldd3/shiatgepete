package com.lasec.monitoreoapp.presentation.screens.checklist

import android.annotation.SuppressLint
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
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.lasec.monitoreoapp.R
import com.lasec.monitoreoapp.data.database.MonitoringDatabase
import com.lasec.monitoreoapp.presentation.screens.actividad_sinorden.SinOrderActivity
import com.lasec.monitoreoapp.presentation.screens.home.HomeActivity
import com.lasec.monitoreoapp.presentation.screens.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@AndroidEntryPoint

class ChecklistActivity : AppCompatActivity() {
    //Variables para la logica del contador de tiempo trans.
    private lateinit var btnIniciar: ImageButton
    private lateinit var tvTiempo: TextView
    private var contador = 0
    private var handlers = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable
    private var contando = false

    //Variables para los iconos de wifi y bluetooth
    private lateinit var ic_wifi: ImageView
    private lateinit var ic_bt: ImageView

    //Variables para la barra de progreso
    private lateinit var tvProgreso: TextView
    private lateinit var progreso: ProgressBar
    private lateinit var avanzar: Button
    private lateinit var reducir: Button
    private var progresoActual = 0

    //Logica para la fecha y hora
    private lateinit var tvReloj: TextView
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

    private fun actualizarWifi() {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

        val wifiEncendido = wifiManager.isWifiEnabled
        val conectadoWifi = networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected

        if (wifiEncendido) {
            if (conectadoWifi) {
                ic_wifi.setImageResource(R.drawable.ic_wifi_on)
            } else {
                ic_wifi.setImageResource(R.drawable.ic_wifi_enabled)
            }
        } else {
            ic_wifi.setImageResource(R.drawable.ic_wifi_off)
        }
    }

    private fun actualizarBluetooth() {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            ic_bt.setImageResource(R.drawable.ic_bluetooth_off)
        } else {
            if (bluetoothAdapter.isEnabled) {
                ic_bt.setImageResource(R.drawable.ic_bluetooth_on)
            } else {
                ic_bt.setImageResource(R.drawable.ic_bluetooth_off)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist)
        // Acceder a los botones del layout incluido
        val filaLuces = findViewById<View>(R.id.include_fila_luces)
        val btnSiLuces = filaLuces.findViewById<ToggleButton>(R.id.btn_si_luces)
        val btnNoLuces = filaLuces.findViewById<ToggleButton>(R.id.btn_no_luces)
        // Mostrar/ocultar checklist eléctrica al tocar el encabezado
        val headerElectrica = findViewById<LinearLayout>(R.id.header_electrica)
        val layoutChecklist = findViewById<LinearLayout>(R.id.layout_checklist_electrica)

        val arrowIcon = findViewById<ImageView>(R.id.arrow_electrica)

        headerElectrica.setOnClickListener {
            if (layoutChecklist.visibility == View.GONE) {
                layoutChecklist.visibility = View.VISIBLE
            } else {
                layoutChecklist.visibility = View.GONE
                arrowIcon.setImageResource(R.drawable.ic_dropdown_3) // el ícono de flecha hacia abajo
            }
        }


// Lógica de selección
        btnSiLuces.setOnClickListener {
            btnSiLuces.isChecked = true
            btnNoLuces.isChecked = false
        }

        btnNoLuces.setOnClickListener {
            btnNoLuces.isChecked = true
            btnSiLuces.isChecked = false
        }

        supportActionBar?.hide()
        esconderBarras()

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

        // Iconos de estado de red
        ic_wifi = findViewById(R.id.ic_wifich)
        ic_bt = findViewById(R.id.ic_bluetoothch)

        actualizarWifi()
        actualizarBluetooth()

        // Botones navegación
        findViewById<ImageButton>(R.id.btn_checklist).setOnClickListener {
            if (this !is ChecklistActivity) {
                startActivity(Intent(this, ChecklistActivity::class.java))
                finish()
            }
        }

        findViewById<ImageButton>(R.id.btn_sinorder).setOnClickListener {
            startActivity(Intent(this, SinOrderActivity::class.java))
        }

        findViewById<ImageButton>(R.id.btn_home).setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Reloj y progreso
        tvReloj = findViewById(R.id.tv_reloj)
        handler.post(relojRunnable)

//        //Logica para mostrar el nombre del usuario con el que se inicio sesión
//        val empleadoId = intent.getIntExtra("dispatchEmployeeId", -1)
//
//        lifecycleScope.launch {
//            val db = Room.databaseBuilder(
//                applicationContext,
//                MonitoringDatabase::class.java,
//                "monitoring_database"
//            ).build()
//
//            val empleado = db.getEmployeesDao().getEmployeeByDispatchId(empleadoId.toString())
//
//            val nombreCompleto = "${empleado.Name} ${empleado.PaternalLastName} ${empleado.MaternalLastName}"
//            val btnNombreUsuario = findViewById<Button>(R.id.btn_userProfile)
//            btnNombreUsuario.text = nombreCompleto
//        }
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
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }

    private fun cerrarSesion(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}