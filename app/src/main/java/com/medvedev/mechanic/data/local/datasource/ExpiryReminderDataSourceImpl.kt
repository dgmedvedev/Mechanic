package com.medvedev.mechanic.data.local.datasource

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.error.toData
import com.medvedev.mechanic.data.local.dao.ExpiryReminderDao
import com.medvedev.mechanic.data.local.entity.ExpiryReminderShownEntity
import com.medvedev.mechanic.domain.result.Result
import javax.inject.Inject

class ExpiryReminderDataSourceImpl @Inject constructor(
    private val dao: ExpiryReminderDao,
) : ExpiryReminderDataSource {

    override suspend fun getAll(): Result<List<ExpiryReminderShownEntity>, DataError> {
        return try {
            Result.Success(dao.getAll())
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    override suspend fun upsertAll(
        items: List<ExpiryReminderShownEntity>,
    ): Result<Unit, DataError> {
        return try {
            dao.upsertAll(items)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    override suspend fun deleteEntity(
        entityType: String,
        entityId: String,
    ): Result<Unit, DataError> {
        return try {
            dao.deleteEntity(entityType, entityId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    override suspend fun deleteField(
        entityType: String,
        entityId: String,
        field: String,
    ): Result<Unit, DataError> {
        return try {
            dao.deleteField(entityType, entityId, field)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }
}
