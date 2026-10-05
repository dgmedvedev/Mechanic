package com.medvedev.mechanic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medvedev.mechanic.data.local.entity.ExpiryReminderShownEntity

@Dao
interface ExpiryReminderDao {

    @Query("SELECT * FROM expiry_reminder_shown")
    suspend fun getAll(): List<ExpiryReminderShownEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ExpiryReminderShownEntity>)

    @Query("DELETE FROM expiry_reminder_shown WHERE entityType = :entityType AND entityId = :entityId")
    suspend fun deleteEntity(entityType: String, entityId: String)

    @Query(
        """
        DELETE FROM expiry_reminder_shown
        WHERE entityType = :entityType AND entityId = :entityId AND field = :field
        """,
    )
    suspend fun deleteField(entityType: String, entityId: String, field: String)
}
