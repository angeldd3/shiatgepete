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

// ⚠️ Formateador 24h forzado en UTC con 'Z' literal
@RequiresApi(Build.VERSION_CODES.O)
private val UTC_24H_FMT: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC)

/**
 * Convierte a ISO-8601 UTC (Z) y aplica desplazamiento "opuesto" por turno:
 *   - workShiftId == 1 -> +6h
 *   - workShiftId == 2 -> -6h
 *   - otros            -> 0h
 *
 * Acepta:
 *  - "HH:mm" o "HH:mm:ss"            -> combina con workDate en UTC
 *  - "yyyy-MM-dd'T'HH:mm[:ss]"       -> interpreta como UTC (sin zona)
 *  - "yyyy-MM-dd'T'HH:mm[:ss]XXX"/Z  -> respeta offset/Z
 *
 * No se asume zona local.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun String.toUtcIsoZ(workDate: LocalDate, workShiftId: Int): String {
    val deltaHours = when (workShiftId) {
        1 -> 6L
        2 -> -6L
        else -> 0L
    }
    fun Instant.applyDelta(): Instant = plusSeconds(deltaHours * 3600)

    // 1) Intentar con OffsetDateTime (maneja ...Z y ...±HH:mm)
    runCatching { OffsetDateTime.parse(this).toInstant() }
        .getOrNull()
        ?.let { return UTC_24H_FMT.format(it.applyDelta()) }

    // 2) Intentar Instant puro (ISO con Z)
    runCatching { Instant.parse(this) }
        .getOrNull()
        ?.let { return UTC_24H_FMT.format(it.applyDelta()) }

    return try {
        if (contains('T')) {
            // 3) LocalDateTime sin zona -> tomar como UTC
            val ldt = LocalDateTime.parse(this, ISO_LOCAL_DT)
            val inst = ldt.toInstant(ZoneOffset.UTC)
            UTC_24H_FMT.format(inst.applyDelta())
        } else {
            // 4) Solo hora -> combinar con workDate (UTC)
            val lt = LocalTime.parse(this, TIME_ONLY)
            val zdtUtc = ZonedDateTime.of(workDate, lt, ZoneOffset.UTC)
            UTC_24H_FMT.format(zdtUtc.toInstant().applyDelta())
        }
    } catch (e: DateTimeParseException) {
        throw e
    }
}

