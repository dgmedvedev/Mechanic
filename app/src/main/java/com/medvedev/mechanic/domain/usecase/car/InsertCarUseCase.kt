package com.medvedev.mechanic.domain.usecase.car

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.model.ExpiryEntityType
import com.medvedev.mechanic.domain.model.ExpiryField
import com.medvedev.mechanic.domain.repository.CarRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import com.medvedev.mechanic.domain.result.Result

class InsertCarUseCase(
    private val repository: CarRepository,
    private val expiryReminderRepository: ExpiryReminderRepository,
    private val scheduler: ExpiryReminderScheduler,
) {
    suspend operator fun invoke(car: Car): Result<Unit, DomainError> {
        val previous = when (val result = repository.getCarById(car.id)) {
            is Result.Success -> result.data
            is Result.Error -> null
        }
        val insert = repository.insertCar(car)
        if (insert is Result.Success) {
            if (previous != null) {
                clearChangedFields(previous, car)
            }
            scheduler.enqueueImmediate()
        }
        return insert
    }

    private suspend fun clearChangedFields(previous: Car, current: Car) {
        val fields = listOf(
            ExpiryField.CHECKUP to (previous.checkup != current.checkup),
            ExpiryField.INSURANCE to (previous.insurance != current.insurance),
            ExpiryField.HULL_INSURANCE to (previous.hullInsurance != current.hullInsurance),
        )
        fields.forEach { (field, changed) ->
            if (changed) {
                expiryReminderRepository.clearField(ExpiryEntityType.CAR, current.id, field)
            }
        }
    }
}
