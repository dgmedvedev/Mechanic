package com.medvedev.mechanic.domain.repository

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.ExpiryEntityType
import com.medvedev.mechanic.domain.model.ExpiryField
import com.medvedev.mechanic.domain.model.ExpiryReminderKey
import com.medvedev.mechanic.domain.result.Result

interface ExpiryReminderRepository {

    suspend fun getShownKeys(): Result<Set<ExpiryReminderKey>, DomainError>

    suspend fun markShown(
        keys: Collection<ExpiryReminderKey>,
        shownOnEpochDay: Long,
    ): Result<Unit, DomainError>

    suspend fun clearEntity(
        entityType: ExpiryEntityType,
        entityId: String,
    ): Result<Unit, DomainError>

    suspend fun clearField(
        entityType: ExpiryEntityType,
        entityId: String,
        field: ExpiryField,
    ): Result<Unit, DomainError>
}
