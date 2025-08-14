package com.lasec.monitoreoapp.domain.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import com.lasec.monitoreoapp.domain.model.WorkShift
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.max



private const val BLOCK_MINUTES = 30
private const val RATE_PER_MIN = 0.75f
// Útil para validar formato "HH:mm" o "HH:mm:ss"
private val TIME_REGEX = Regex("\\d{2}:\\d{2}(:\\d{2})?")
fun isTimeFormat(text: String) = text.matches(TIME_REGEX)

// Ya tienes minutesBetween(start, end) en este paquete.
// Asegúrate de que acepte "HH:mm[:ss]".

@RequiresApi(Build.VERSION_CODES.O)
fun calcQuantity(start: String, end: String): Float {
    val mins = minutesBetween(start, end) // <- tu función ya existente
    return mins * RATE_PER_MIN
}

/**
 * Verifica si 'end' es una opción válida de fin para el turno dado y el 'start'.
 * Útil cuando el usuario cambia la hora de inicio: puedes checar si el fin actual sigue siendo válido.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun isEndValidForShift(
    shift: com.lasec.monitoreoapp.domain.model.WorkShift,
    start: String,
    end: String
): Boolean {
    val opciones = generateFinalTimeSlotsEvery30Min(shift, start)
    return opciones.contains(end)
}


@RequiresApi(Build.VERSION_CODES.O)
fun endTimeFromQuantityExact(shift: WorkShift, startHHmmOrHHmmss: String, quantity: Float): String? {
    if (quantity <= 0f) return null

    val inFmt = DateTimeFormatter.ofPattern("HH:mm[:ss]")
    val outFmt = DateTimeFormatter.ofPattern("HH:mm:ss")

    val sStart = LocalTime.parse(shift.startTime, inFmt).withSecond(0).withNano(0)
    val sEnd   = LocalTime.parse(shift.endTime,   inFmt).withSecond(0).withNano(0)
    val crossesMidnight = sEnd.isBefore(sStart)

    val today = LocalDate.now()
    val start = LocalTime.parse(startHHmmOrHHmmss, inFmt).withSecond(1).withNano(0)
    val startDT = LocalDateTime.of(today, start)

    val shiftEndDT = if (crossesMidnight)
        LocalDateTime.of(today.plusDays(1), sEnd)
    else
        LocalDateTime.of(today, sEnd)

    val minsExact = kotlin.math.round((quantity / RATE_PER_MIN)).toLong()
    val candidate = startDT.plusMinutes(minsExact.toLong())

    if (candidate.isAfter(shiftEndDT)) return null
    return candidate.toLocalTime().withSecond(0).withNano(0).format(outFmt)
}

