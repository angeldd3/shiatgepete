package com.lasec.monitoreoapp.domain.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import com.lasec.monitoreoapp.domain.model.WorkShift
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun WorkShift.generateSlotsEvery30Min(): List<String> {
    val inFmt = DateTimeFormatter.ofPattern("HH:mm[:ss]")
    val outFmt = DateTimeFormatter.ofPattern("HH:mm:ss")

    val start = LocalTime.parse(startTime, inFmt).withSecond(0).withNano(0)
    val end   = LocalTime.parse(endTime, inFmt).withSecond(0).withNano(0)

    val today = LocalDate.now()
    var current = LocalDateTime.of(today, start)

    val crossesMidnight = end.isBefore(start)
    val endDateTime = if (crossesMidnight)
        LocalDateTime.of(today.plusDays(1), end)
    else
        LocalDateTime.of(today, end)

    val slots = mutableListOf<String>()
    while (!current.isAfter(endDateTime)) {
        slots.add(current.toLocalTime().withSecond(1).withNano(0).format(outFmt))
        current = current.plusMinutes(30)
    }
    return slots
}
