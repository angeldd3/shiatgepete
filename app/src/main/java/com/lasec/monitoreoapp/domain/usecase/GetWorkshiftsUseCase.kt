package com.lasec.monitoreoapp.domain.usecase

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.lasec.monitoreoapp.data.database.entities.toDatabase
import com.lasec.monitoreoapp.data.repository.WorkShiftsRepository
import com.lasec.monitoreoapp.domain.model.Employee
import com.lasec.monitoreoapp.domain.model.WorkShift
import javax.inject.Inject

class GetWorkshiftsUseCase @Inject constructor(private val repository: WorkShiftsRepository) {

    suspend operator fun invoke(): List<WorkShift> {
        return try {
            // 1. Intentar obtener datos de la APIDE
            val workShifs = repository.getAllWorkShiftsFromApi()

            // 2. Si la API responde con datos
            if (workShifs.isNotEmpty()) {
                repository.clearWorkShifts()
                repository.insertWorkShifts(workShifs.map { it.toDatabase() })
                workShifs // Devolver datos frescos
            } else {
                // 3. Si la API devuelve vacío (¿error o datos válidos?)
                repository.getAllWorkShiftsFromDatabase() // Fallback a caché
            }
        } catch (e: Exception) {
            // 4. Manejar errores (red, servidor, etc.)
            Log.e("GetEmployeesUseCase", "Error al obtener empleados", e)
            // También puedes imprimirlo en consola si no tienes Log
            println("Error al obtener empleados: ${e.message}")
            repository.getAllWorkShiftsFromDatabase() // Usar datos locales
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTurnoActual(): WorkShift? {
        val turnos = invoke() // trae de API o DB (según tu lógica)
        if (turnos.isEmpty()) return null

        val inFmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm[:ss]")
        val ahora = java.time.LocalTime.now() // hora local del dispositivo

        return turnos.firstOrNull { t ->
            val inicio = java.time.LocalTime.parse(t.startTime, inFmt)
            val finConSeg = java.time.LocalTime.parse(t.endTime, inFmt)
            // Normaliza fin al minuto para evitar cosas como 20:30:01
            val fin = finConSeg.withSecond(0).withNano(0)

            when {
                // Si start==end pero las cadenas difieren → lo tomamos como turno 24h
                inicio == fin && t.startTime != t.endTime -> true
                // Turno en el mismo día
                inicio < fin -> !ahora.isBefore(inicio) && !ahora.isAfter(fin)
                // Cruza medianoche (inicio > fin)
                else -> !ahora.isBefore(inicio) || !ahora.isAfter(fin)
            }
        }
    }


}