package com.lasec.monitoreoapp.domain.extensions.manual_workorders

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
private val TIME_ONLY: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm[:ss]")
@RequiresApi(Build.VERSION_CODES.O)
private val ISO_LOCAL_DT: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME // 2025-08-12T16:30:00
@RequiresApi(Build.VERSION_CODES.O)
private val ISO_INSTANT_FMT: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT
@RequiresApi(Build.VERSION_CODES.O)
val MX_ZONE: ZoneId = ZoneId.of("America/Mexico_City")

/**
 * Convierte distintos formatos de entrada a ISO-8601 UTC (terminado en Z).
 * Acepta:
 *  - "HH:mm" o "HH:mm:ss"  (interpreta la fecha como 'workDate' en zona MX)
 *  - "yyyy-MM-dd'T'HH:mm[:ss]" (asume zona MX)
 *  - "yyyy-MM-dd'T'HH:mm[:ss]XXX" o "....Z" (usa el offset/UTC provisto)
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.toUtcIsoZ(workDate: LocalDate = LocalDate.now(MX_ZONE)): String {
    return try {
        when {
            // Caso 1: contiene offset o Z (ej: 2025-08-12T16:30:00Z o 2025-08-12T16:30:00-06:00)
            endsWith("Z") || contains('+') || substringAfterLast('T', "").contains('+') -> {
                // Intentar OffsetDateTime primero
                val odt = OffsetDateTime.parse(this)
                ISO_INSTANT_FMT.format(odt.toInstant())
            }
            // Caso 2: contiene 'T' pero sin Z/offset -> ISO local date-time
            contains('T') -> {
                val ldt = LocalDateTime.parse(this, ISO_LOCAL_DT)
                val zdt = ldt.atZone(MX_ZONE)
                ISO_INSTANT_FMT.format(zdt.toInstant())
            }
            // Caso 3: solo hora -> usar workDate
            else -> {
                val lt = LocalTime.parse(this, TIME_ONLY)
                val zdt = ZonedDateTime.of(workDate, lt, MX_ZONE)
                ISO_INSTANT_FMT.format(zdt.toInstant())
            }
        }
    } catch (e: DateTimeParseException) {
        // fallback pequeño: intenta parsear como Instant si viniera en otro ISO válido
        try {
            val inst = Instant.parse(this)
            ISO_INSTANT_FMT.format(inst)
        } catch (_: Throwable) {
            throw e // re-lanza el error original para depurar
        }
    }
}
