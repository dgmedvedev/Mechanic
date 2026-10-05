package com.medvedev.mechanic.data.repository

import com.medvedev.mechanic.data.error.toDomain
import com.medvedev.mechanic.data.local.datasource.ExpiryReminderDataSource
import com.medvedev.mechanic.data.local.mapper.toDomainOrNull
import com.medvedev.mechanic.data.local.mapper.toEntity
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.ExpiryEntityType
import com.medvedev.mechanic.domain.model.ExpiryField
import com.medvedev.mechanic.domain.model.ExpiryReminderKey
import com.medvedev.mechanic.domain.repository.ExpiryReminderRepository
import com.medvedev.mechanic.domain.result.Result
import javax.inject.Inject

class ExpiryReminderRepositoryImpl @Inject constructor(
    private val dataSource: ExpiryReminderDataSource
) : ExpiryReminderRepository {

    override suspend fun getShownKeys(): Result<Set<ExpiryReminderKey>, DomainError> {
        return when (val result = dataSource.getAll()) {
            is Result.Success -> Result.Success(
                result.data.mapNotNull { it.toDomainOrNull() }.toSet(),
            )

            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun markShown(
        keys: Collection<ExpiryReminderKey>,
        shownOnEpochDay: Long,
    ): Result<Unit, DomainError> {
        if (keys.isEmpty()) return Result.Success(Unit)
        return when (val result = dataSource.upsertAll(keys.map { it.toEntity(shownOnEpochDay) })) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun clearEntity(
        entityType: ExpiryEntityType,
        entityId: String,
    ): Result<Unit, DomainError> {
        return when (val result = dataSource.deleteEntity(entityType.name, entityId)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun clearField(
        entityType: ExpiryEntityType,
        entityId: String,
        field: ExpiryField,
    ): Result<Unit, DomainError> {
        return when (val result = dataSource.deleteField(entityType.name, entityId, field.name)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }
}
