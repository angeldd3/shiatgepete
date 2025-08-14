package com.lasec.monitoreoapp.presentation.screens.Incidencia

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.lasec.monitoreoapp.R
import com.lasec.monitoreoapp.presentation.screens.actividad_sinorden.SinOrderActivity
import com.lasec.monitoreoapp.presentation.screens.home.HomeActivity

class RegistroIncidenciaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_incidencia)

        val spinnerTipoFalla = findViewById<Spinner>(R.id.spinner_tipo_falla)
        val spinnerMotivo = findViewById<Spinner>(R.id.spinner_motivo)

        // Definimos los tipos de falla
        val tiposFalla = listOf("Selecciona un tipo...", "Equipo", "Personal", "Operativa")
        val adapterTipos = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposFalla)
        adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoFalla.adapter = adapterTipos

        // Diccionario de motivos según el tipo
        val motivosPorTipo = mapOf(
            "Equipo" to listOf("Motor dañado", "Neumáticos averiados", "Sistema eléctrico"),
            "Personal" to listOf("Ausencia injustificada", "Sin EPP", "Falta de capacitación"),
            "Operativa" to listOf("Ruta bloqueada", "Condiciones inseguras", "Falla en señalización")
        )

        val btnBaja = findViewById<Button>(R.id.btnBaja)
        val btnMedia = findViewById<Button>(R.id.btnMedia)
        val btnAlta = findViewById<Button>(R.id.btnAlta)

        val colorNormal = Color.parseColor("#143C66")
        val colorPresionado = Color.parseColor("#6DAFC6")

        val botones = listOf(btnBaja, btnMedia, btnAlta)

        botones.forEach { boton ->
            boton.setBackgroundColor(colorNormal) // color inicial
            boton.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        boton.setBackgroundColor(colorPresionado)
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        boton.setBackgroundColor(colorNormal)
                    }
                }
                false // para que el click funcione normalmente
            }
        }

        val botonMostrarDialogo = findViewById<Button>(R.id.btn_enviar_incidencia)
        botonMostrarDialogo.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_registroincidencia, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).create()

            val btnAceptar = dialogView.findViewById<Button>(R.id.btn_aceptar)

            btnAceptar.setOnClickListener {
                startActivity(Intent(this, SinOrderActivity::class.java))
                dialog.dismiss()
            }
            dialog.show()
            dialog.window?.setDimAmount(0.2f)
        }

        // Inhabilitar spinner de motivo al inicio
        spinnerMotivo.isEnabled = false
        val btnEnviar = findViewById<Button>(R.id.btn_enviar_incidencia)

        btnEnviar.setOnClickListener {
            val tipo = spinnerTipoFalla.selectedItem.toString()
            val motivo = spinnerMotivo.selectedItem.toString()

            Toast.makeText(this, "Incidencia: $tipo - $motivo", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }



        // Listener del tipo de falla
        spinnerTipoFalla.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val seleccion = tiposFalla[position]

                if (seleccion != "Selecciona un tipo...") {
                    val motivos = motivosPorTipo[seleccion] ?: listOf()
                    val adapterMotivo = ArrayAdapter(
                        this@RegistroIncidenciaActivity,
                        android.R.layout.simple_spinner_item,
                        motivos
                    )
                    adapterMotivo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerMotivo.adapter = adapterMotivo
                    spinnerMotivo.isEnabled = true
                } else {
                    spinnerMotivo.adapter = null
                    spinnerMotivo.isEnabled = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
