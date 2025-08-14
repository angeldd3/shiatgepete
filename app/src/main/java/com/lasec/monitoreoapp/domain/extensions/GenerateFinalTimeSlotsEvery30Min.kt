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



@RequiresApi(Build.VERSION_CODES.O)
fun minutesBetween(startHHmmOrHHmmss: String, endHHmmss: String): Long {
    val f = java.time.format.DateTimeFormatter.ofPattern("HH:mm[:ss]")
    val start = java.time.LocalTime.parse(startHHmmOrHHmmss, f)
    val end = java.time.LocalTime.parse(endHHmmss, f)
    val today = java.time.LocalDate.now()
    var endDT = java.time.LocalDateTime.of(today, end)
    val startDT = java.time.LocalDateTime.of(today, start)
    if (end.isBefore(start) || end == start) endDT = endDT.plusDays(1)
    return java.time.Duration.between(startDT, endDT).toMinutes()
}


/** Final time options: every 30 min from 'start' to the END of the shift (handles crossing midnight). */
@RequiresApi(Build.VERSION_CODES.O)
fun generateFinalTimeSlotsEvery30Min(
    shift: com.lasec.monitoreoapp.domain.model.WorkShift,
    startHHmmOrHHmmss: String          // <- nombre más fiel a lo que acepta
): List<String> {
    val inFmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm[:ss]")

    val shiftStart = java.time.LocalTime.parse(shift.startTime, inFmt)
    val shiftEndRaw = java.time.LocalTime.parse(shift.endTime, inFmt).withSecond(0).withNano(0)
    val crossesMidnight = shiftEndRaw.isBefore(shiftStart)

    val today = java.time.LocalDate.now()


    val selectedStart = java.time.LocalTime.parse(startHHmmOrHHmmss, inFmt)
        .withSecond(0).withNano(0)

    var current = java.time.LocalDateTime.of(today, selectedStart).plusMinutes(30)

    val shiftEndDT = if (crossesMidnight)
        java.time.LocalDateTime.of(today.plusDays(1), shiftEndRaw)
    else
        java.time.LocalDateTime.of(today, shiftEndRaw)

    val options = mutableListOf<String>()
    val outFmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
    while (!current.isAfter(shiftEndDT)) {
        // fin siempre con :01
        options.add(current.toLocalTime().withSecond(0).withNano(0).format(outFmt))
        current = current.plusMinutes(30)
    }
    return options
}


@RequiresApi(Build.VERSION_CODES.O)
private fun formatEndTimeWithSecond01(t: java.time.LocalTime): String =  // <-- puede quedar private
    t.withSecond(1).withNano(0)
        .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"))






