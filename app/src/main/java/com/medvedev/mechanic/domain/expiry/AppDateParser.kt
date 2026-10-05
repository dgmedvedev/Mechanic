package com.medvedev.mechanic.domain.expiry

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import java.util.Locale

object AppDateParser {
    private val formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd.MM.uuuu", Locale.US)
            .withResolverStyle(ResolverStyle.STRICT)

    fun parse(value: String): LocalDate? {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return null
        return try {
            LocalDate.parse(trimmed, formatter)
        } catch (_: DateTimeParseException) {
            null
        }
    }
}
