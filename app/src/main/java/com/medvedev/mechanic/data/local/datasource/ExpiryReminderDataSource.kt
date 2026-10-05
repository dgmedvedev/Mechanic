package com.medvedev.mechanic.data.local.datasource

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.local.entity.ExpiryReminderShownEntity
import com.medvedev.mechanic.domain.result.Result

interface ExpiryReminderDataSource {

    suspend fun getAll(): Result<List<ExpiryReminderShownEntity>, DataError>

    suspend fun upsertAll(items: List<ExpiryReminderShownEntity>): Result<Unit, DataError>

    suspend fun deleteEntity(entityType: String, entityId: String): Result<Unit, DataError>

    suspend fun deleteField(
        entityType: String,
        entityId: String,
        field: String,
    ): Result<Unit, DataError>
}
