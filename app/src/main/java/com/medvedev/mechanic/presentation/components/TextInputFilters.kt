package com.medvedev.mechanic.presentation.components

import java.util.Locale

enum class DetailInputType {
    Text,
    CapitalizeFirst,
    Uppercase,
    Year,
    Decimal,
    FuelRate,
}

object TextInputFilters {
    fun capitalizeFirst(value: String): String =
        value.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
        }

    fun uppercase(value: String): String = value.uppercase(Locale.getDefault())

    fun year(value: String): String = value.filter { it.isDigit() }.take(4)

    fun decimal(value: String): String {
        var separatorUsed = false
        return buildString(value.length) {
            for (char in value) {
                when {
                    char.isDigit() -> append(char)
                    (char == ',' || char == '.') && !separatorUsed -> {
                        if (isEmpty()) append('0')
                        append(',')
                        separatorUsed = true
                    }
                }
            }
        }
    }

    fun finalizeDecimal(value: String): String = decimal(value).trimEnd(',')

    fun formatFuelRate(value: String): String {
        val filtered = decimal(value)
        if (filtered.isEmpty()) return ""
        val separatorIndex = filtered.indexOf(',')
        return if (separatorIndex < 0) {
            "$filtered,0"
        } else {
            val integer = filtered.substring(0, separatorIndex).ifEmpty { "0" }
            val fraction = filtered.substring(separatorIndex + 1)
            "$integer,${fraction.ifEmpty { "0" }}"
        }
    }
}

fun DetailInputType.filter(value: String): String = when (this) {
    DetailInputType.Text -> value
    DetailInputType.CapitalizeFirst -> TextInputFilters.capitalizeFirst(value)
    DetailInputType.Uppercase -> TextInputFilters.uppercase(value)
    DetailInputType.Year -> TextInputFilters.year(value)
    DetailInputType.Decimal,
    DetailInputType.FuelRate,
        -> TextInputFilters.decimal(value)
}

fun DetailInputType.finish(value: String): String = when (this) {
    DetailInputType.FuelRate -> TextInputFilters.formatFuelRate(value)
    DetailInputType.Decimal -> TextInputFilters.finalizeDecimal(value)
    else -> filter(value)
}
