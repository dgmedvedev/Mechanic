package com.medvedev.mechanic.domain.usecase.car

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.model.ExpiryEntityType
import com.medvedev.mechanic.domain.repository.CarRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import com.medvedev.mechanic.domain.result.Result

class DeleteCarUseCase(
    private val repository: CarRepository,
    private val expiryReminderRepository: ExpiryReminderRepository,
    private val scheduler: ExpiryReminderScheduler,
) {
    suspend operator fun invoke(car: Car): Result<Unit, DomainError> {
        val result = repository.deleteCar(car)
        if (result is Result.Success) {
            expiryReminderRepository.clearEntity(ExpiryEntityType.CAR, car.id)
            scheduler.enqueueImmediate()
        }
        return result
    }
}
