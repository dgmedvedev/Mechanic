package com.medvedev.mechanic.domain.usecase.expiry

import com.medvedev.mechanic.domain.expiry.AppDateParser
import com.medvedev.mechanic.domain.expiry.ExpiryThresholdMatcher
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.model.ExpiryEntityType
import com.medvedev.mechanic.domain.model.ExpiryEvent
import com.medvedev.mechanic.domain.model.ExpiryField
import com.medvedev.mechanic.domain.model.ExpiryThreshold
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class GetUpcomingExpirationsUseCase {

    operator fun invoke(
        cars: List<Car>,
        drivers: List<Driver>,
        today: LocalDate,
        thresholds: Set<ExpiryThreshold>,
    ): List<ExpiryEvent> {
        if (thresholds.isEmpty()) return emptyList()
        val events = buildList {
            cars.forEach { car -> addAll(car.toEvents(today, thresholds)) }
            drivers.forEach { driver -> addAll(driver.toEvents(today, thresholds)) }
        }
        return events.sortedWith(
            compareBy<ExpiryEvent> { it.daysUntil }
                .thenBy { it.subjectName }
                .thenBy { it.field.name },
        )
    }

    private fun Car.toEvents(
        today: LocalDate,
        thresholds: Set<ExpiryThreshold>,
    ): List<ExpiryEvent> {
        val name = carSubjectName()
        return listOf(
            ExpiryField.CHECKUP to checkup,
            ExpiryField.INSURANCE to insurance,
            ExpiryField.HULL_INSURANCE to hullInsurance,
        ).mapNotNull { (field, raw) ->
            toEvent(ExpiryEntityType.CAR, id, name, field, raw, today, thresholds)
        }
    }

    private fun Driver.toEvents(
        today: LocalDate,
        thresholds: Set<ExpiryThreshold>,
    ): List<ExpiryEvent> {
        val name = driverSubjectName()
        return listOf(
            ExpiryField.DRIVING_LICENSE to drivingLicenseValidity,
            ExpiryField.MEDICAL_CERTIFICATE to medicalCertificateValidity,
        ).mapNotNull { (field, raw) ->
            toEvent(ExpiryEntityType.DRIVER, id, name, field, raw, today, thresholds)
        }
    }

    private fun toEvent(
        entityType: ExpiryEntityType,
        entityId: String,
        subjectName: String,
        field: ExpiryField,
        rawDate: String,
        today: LocalDate,
        thresholds: Set<ExpiryThreshold>,
    ): ExpiryEvent? {
        val date = AppDateParser.parse(rawDate) ?: return null
        val daysUntil = ChronoUnit.DAYS.between(today, date)
        val threshold = ExpiryThresholdMatcher.matching(daysUntil, thresholds) ?: return null
        return ExpiryEvent(
            entityType = entityType,
            entityId = entityId,
            subjectName = subjectName,
            field = field,
            date = date,
            daysUntil = daysUntil,
            threshold = threshold,
        )
    }

    private fun Car.carSubjectName(): String {
        val title = listOf(brand, model).filter { it.isNotBlank() }.joinToString(" ")
        return when {
            title.isNotBlank() && stateNumber.isNotBlank() -> "$title ($stateNumber)"
            title.isNotBlank() -> title
            else -> stateNumber
        }
    }

    private fun Driver.driverSubjectName(): String =
        listOf(surname, name, middleName).filter { it.isNotBlank() }.joinToString(" ")
}
