package com.medvedev.mechanic.domain.usecase.driver

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.model.ExpiryEntityType
import com.medvedev.mechanic.domain.model.ExpiryField
import com.medvedev.mechanic.domain.repository.DriverRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import com.medvedev.mechanic.domain.result.Result

class InsertDriverUseCase(
    private val repository: DriverRepository,
    private val expiryReminderRepository: ExpiryReminderRepository,
    private val scheduler: ExpiryReminderScheduler,
) {
    suspend operator fun invoke(driver: Driver): Result<Unit, DomainError> {
        val previous = when (val result = repository.getDriverById(driver.id)) {
            is Result.Success -> result.data
            is Result.Error -> null
        }
        val insert = repository.insertDriver(driver)
        if (insert is Result.Success) {
            if (previous != null) {
                clearChangedFields(previous, driver)
            }
            scheduler.enqueueImmediate()
        }
        return insert
    }

    private suspend fun clearChangedFields(previous: Driver, current: Driver) {
        val fields = listOf(
            ExpiryField.DRIVING_LICENSE to
                (previous.drivingLicenseValidity != current.drivingLicenseValidity),
            ExpiryField.MEDICAL_CERTIFICATE to
                (previous.medicalCertificateValidity != current.medicalCertificateValidity),
        )
        fields.forEach { (field, changed) ->
            if (changed) {
                expiryReminderRepository.clearField(ExpiryEntityType.DRIVER, current.id, field)
            }
        }
    }
}
