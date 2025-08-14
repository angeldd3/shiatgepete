package com.lasec.monitoreoapp.domain.extensions.manual_workorders

import android.os.Build
import androidx.annotation.RequiresApi
import com.lasec.monitoreoapp.domain.model.WorkShift
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun WorkShift.extractHMS(): WorkShiftHMS {
    val f = DateTimeFormatter.ofPattern("HH:mm[:ss]")
    val startLT = LocalTime.parse(this.startTime, f)
    val endLT = LocalTime.parse(this.endTime, f)
    return WorkShiftHMS(
        startHour = startLT.hour,
        startMinute = startLT.minute,
        startSecond = startLT.second,
        endHour = endLT.hour,
        endMinute = endLT.minute,
        endSecond = endLT.second
    )
}

data class WorkShiftHMS(
    val startHour: Int,
    val startMinute: Int,
    val startSecond: Int,
    val endHour: Int,
    val endMinute: Int,
    val endSecond: Int
)
