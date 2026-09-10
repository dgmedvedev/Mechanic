package com.medvedev.mechanic.data.backup

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.core.net.toUri
import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.error.toData
import com.medvedev.mechanic.data.local.database.AppDatabase
import com.medvedev.mechanic.domain.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DatabaseBackupDataSourceImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val database: AppDatabase,
) : DatabaseBackupDataSource {

    override suspend fun createSnapshot(): Result<CachedBackup, DataError> =
        withContext(Dispatchers.IO) {
            try {
                val backupsDir = backupsDir()
                backupsDir.listFiles()?.forEach { it.delete() }
                val displayName = backupDisplayName()
                val snapshot = File(backupsDir, displayName)
                writeConsistentSnapshot(snapshot)
                if (!snapshot.isFile || snapshot.length() == 0L) {
                    snapshot.delete()
                    return@withContext Result.Error(DataError.File.Unavailable)
                }
                Result.Success(CachedBackup(file = snapshot, displayName = displayName))
            } catch (e: Exception) {
                Result.Error(e.toData())
            }
        }

    override suspend fun writeToUri(path: String, destinationUri: String): Result<Unit, DataError> =
        withContext(Dispatchers.IO) {
            try {
                val source = File(path)
                if (!source.isFile || source.length() == 0L) {
                    return@withContext Result.Error(DataError.File.Unavailable)
                }
                val uri = destinationUri.toUri()
                val output = context.contentResolver.openOutputStream(uri)
                    ?: return@withContext Result.Error(DataError.File.Unavailable)
                output.use { stream ->
                    source.inputStream().use { input ->
                        copy(input, stream)
                    }
                }
                Result.Success(Unit)
            } catch (e: SecurityException) {
                Result.Error(DataError.File.Unavailable)
            } catch (e: Exception) {
                Result.Error(e.toData())
            }
        }

    override suspend fun restoreFromUri(sourceUri: String): Result<Unit, DataError> =
        withContext(Dispatchers.IO) {
            val incoming = File(backupsDir(), RESTORE_TEMP_NAME)
            try {
                val uri = sourceUri.toUri()
                val input = context.contentResolver.openInputStream(uri)
                    ?: return@withContext Result.Error(DataError.File.Unavailable)
                input.use { stream ->
                    incoming.outputStream().use { output ->
                        copy(stream, output, maxBytes = MAX_BACKUP_BYTES)
                    }
                }
                if (incoming.length() == 0L) {
                    return@withContext Result.Error(DataError.File.Invalid)
                }
                validateBackup(incoming)?.let { error ->
                    return@withContext Result.Error(error)
                }
                replaceDatabase(incoming)
                Result.Success(Unit)
            } catch (e: BackupLimitExceededException) {
                Result.Error(DataError.File.Invalid)
            } catch (e: SecurityException) {
                Result.Error(DataError.File.Unavailable)
            } catch (e: Exception) {
                Result.Error(e.toData())
            } finally {
                incoming.delete()
            }
        }

    private fun writeConsistentSnapshot(destination: File) {
        val dbFile = databaseFile()
        destination.delete()
        File(destination.path + WAL_SUFFIX).delete()
        File(destination.path + SHM_SUFFIX).delete()
        dbFile.copyTo(destination, overwrite = true)
        copyIfExists(File(dbFile.path + WAL_SUFFIX), File(destination.path + WAL_SUFFIX))
        copyIfExists(File(dbFile.path + SHM_SUFFIX), File(destination.path + SHM_SUFFIX))
        SQLiteDatabase.openDatabase(
            destination.path,
            null,
            SQLiteDatabase.OPEN_READWRITE,
        ).use { copy ->
            copy.rawQuery("PRAGMA wal_checkpoint(TRUNCATE)", null).use { it.moveToFirst() }
        }
        File(destination.path + WAL_SUFFIX).delete()
        File(destination.path + SHM_SUFFIX).delete()
    }

    private fun copyIfExists(source: File, destination: File) {
        if (source.exists()) source.copyTo(destination, overwrite = true) else destination.delete()
    }

    private fun validateBackup(file: File): DataError? {
        if (!hasSqliteHeader(file)) return DataError.File.Invalid
        var sqlite: SQLiteDatabase? = null
        return try {
            sqlite = SQLiteDatabase.openDatabase(
                file.path,
                null,
                SQLiteDatabase.OPEN_READONLY,
            )
            when {
                sqlite.version != AppDatabase.VERSION -> DataError.Database.Incompatible
                !hasRequiredTables(sqlite) -> DataError.File.Invalid
                else -> null
            }
        } catch (_: Exception) {
            DataError.File.Invalid
        } finally {
            sqlite?.close()
        }
    }

    private fun hasRequiredTables(sqlite: SQLiteDatabase): Boolean {
        sqlite.rawQuery(
            "SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' AND name IN ('cars', 'drivers')",
            null,
        ).use { cursor ->
            return cursor.moveToFirst() && cursor.getInt(0) == 2
        }
    }

    private fun replaceDatabase(validated: File) {
        val dbFile = databaseFile()
        dbFile.parentFile?.mkdirs()
        val incoming = File(dbFile.parentFile, "${AppDatabase.NAME}.incoming")
        validated.copyTo(incoming, overwrite = true)
        if (database.isOpen) {
            database.close()
        }
        File(dbFile.path + WAL_SUFFIX).delete()
        File(dbFile.path + SHM_SUFFIX).delete()
        if (!incoming.renameTo(dbFile)) {
            incoming.copyTo(dbFile, overwrite = true)
            incoming.delete()
        }
        File(dbFile.path + WAL_SUFFIX).delete()
        File(dbFile.path + SHM_SUFFIX).delete()
    }

    private fun backupsDir(): File =
        File(context.cacheDir, BACKUPS_DIR).apply { mkdirs() }

    private fun databaseFile(): File = context.getDatabasePath(AppDatabase.NAME)

    private fun backupDisplayName(): String {
        val stamp = SimpleDateFormat(DISPLAY_NAME_PATTERN, Locale.US).format(Date())
        return "mechanic-backup-$stamp.db"
    }

    private fun hasSqliteHeader(file: File): Boolean {
        val header = ByteArray(SQLITE_HEADER.size)
        file.inputStream().use { stream ->
            val read = stream.read(header)
            if (read != header.size) return false
        }
        return header.contentEquals(SQLITE_HEADER)
    }

    private fun copy(
        input: InputStream,
        output: OutputStream,
        maxBytes: Long = Long.MAX_VALUE,
    ) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var total = 0L
        while (true) {
            val read = input.read(buffer)
            if (read < 0) break
            total += read
            if (total > maxBytes) throw BackupLimitExceededException()
            output.write(buffer, 0, read)
        }
        output.flush()
    }

    private class BackupLimitExceededException : Exception()

    private companion object {
        const val BACKUPS_DIR = "backups"
        const val RESTORE_TEMP_NAME = "restore-incoming.db"
        const val WAL_SUFFIX = "-wal"
        const val SHM_SUFFIX = "-shm"
        const val DISPLAY_NAME_PATTERN = "yyyy-MM-dd-HHmm"
        const val MAX_BACKUP_BYTES = 50L * 1024 * 1024
        val SQLITE_HEADER = "SQLite format 3\u0000".toByteArray()
    }
}
