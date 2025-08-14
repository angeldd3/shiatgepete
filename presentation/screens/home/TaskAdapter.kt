package com.lasec.monitoreoapp.presentation.screens.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.lasec.monitoreoapp.R
import com.lasec.monitoreoapp.domain.model.TaskFullDetail
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TaskAdapter(private val tasks: List<TaskFullDetail>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPlaceName: TextView = itemView.findViewById(R.id.tvSitioOrigen)
        val tvEconomicNumber: TextView = itemView.findViewById(R.id.tvNumEconomico)
        val tvActivityName: TextView = itemView.findViewById(R.id.tvTipoActividad)
        val tvInitTime: TextView = itemView.findViewById(R.id.tvHoraInicio)

        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_actividad, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = tasks.size

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        val parsedTime = LocalDateTime.parse(task.initTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val zonedTime = parsedTime.atZone(ZoneId.of("UTC"))
        val localTime = zonedTime.withZoneSameInstant(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("hh:mm")
        val formatted = localTime.format(formatter)
        Log.d("HORA", "Hora Formatiada: $formatted")

        holder.tvPlaceName.text = task.placeName
        holder.tvEconomicNumber.text = task.economicNumber
        holder.tvActivityName.text = task.activityName
        holder.tvInitTime.text = "$formatted"

        val statusName = task.activityStatusName ?: "Sin estado"
        holder.tvEstado.text = statusName


        val context = holder.itemView.context
        val color = when (statusName.lowercase()) {
            "in progress"     -> context.getColor(R.color.light_blue_light_blue_500)
            "on hold"         -> context.getColor(R.color.orange_orange_500)
            "canceled"        -> context.getColor(R.color.red)
            "completed"       -> context.getColor(R.color.green)
            "rescheduled"     -> context.getColor(R.color.purple_500)
            else              -> context.getColor(android.R.color.white)
        }


        holder.tvEstado.setTextColor(color)
    }


}
