package com.medvedev.mechanic.presentation.settings

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.model.BackupFile
import com.medvedev.mechanic.presentation.components.ConfirmDialog
import com.medvedev.mechanic.presentation.components.MechanicTopBar
import com.medvedev.mechanic.presentation.components.OverflowMenu
import com.medvedev.mechanic.presentation.error.toMessageRes
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme
import java.io.File

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val resources = LocalResources.current
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showRestoreConfirm by rememberSaveable { mutableStateOf(false) }

    val createDocument = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument(BACKUP_MIME_TYPE),
    ) { uri ->
        uri?.let { viewModel.saveBackup(it.toString()) }
    }
    val openDocument = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri?.let { viewModel.restoreBackup(it.toString()) }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(resources.getString(error.toMessageRes()))
            viewModel.consumeError()
        }
    }
    LaunchedEffect(uiState.backupSaved) {
        if (uiState.backupSaved) {
            snackbarHostState.showSnackbar(resources.getString(R.string.settings_backup_saved))
            viewModel.consumeBackupSaved()
        }
    }
    LaunchedEffect(uiState.restoreCompleted) {
        if (uiState.restoreCompleted) {
            context.restartApp()
        }
    }

    SettingsContent(
        isBusy = uiState.isBusy,
        createdBackup = uiState.createdBackup,
        snackbarHostState = snackbarHostState,
        showRestoreConfirm = showRestoreConfirm,
        onBack = onBack,
        onCreateBackup = viewModel::exportBackup,
        onRestoreClick = { showRestoreConfirm = true },
        onRestoreDismiss = { showRestoreConfirm = false },
        onRestoreConfirm = {
            showRestoreConfirm = false
            openDocument.launch(BACKUP_OPEN_MIME_TYPES)
        },
        onSaveBackup = { backup ->
            createDocument.launch(backup.displayName)
        },
        onShareBackup = { backup ->
            shareBackup(context, backup)
            viewModel.dismissCreatedBackup()
        },
        onDismissCreatedBackup = viewModel::dismissCreatedBackup,
    )
}

@Composable
private fun SettingsContent(
    isBusy: Boolean,
    createdBackup: BackupFile?,
    snackbarHostState: SnackbarHostState,
    showRestoreConfirm: Boolean,
    onBack: () -> Unit,
    onCreateBackup: () -> Unit,
    onRestoreClick: () -> Unit,
    onRestoreDismiss: () -> Unit,
    onRestoreConfirm: () -> Unit,
    onSaveBackup: (BackupFile) -> Unit,
    onShareBackup: (BackupFile) -> Unit,
    onDismissCreatedBackup: () -> Unit,
) {
    Scaffold(
        topBar = {
            MechanicTopBar(
                title = stringResource(R.string.settings),
                onBack = onBack,
                actions = { OverflowMenu() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column {
                Text(
                    text = stringResource(R.string.settings_section_data),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_backup_create)) },
                    supportingContent = {
                        Text(stringResource(R.string.settings_backup_create_hint))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.FileUpload,
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier.clickableIfEnabled(!isBusy, onCreateBackup),
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text(stringResource(R.string.settings_backup_restore)) },
                    supportingContent = {
                        Text(stringResource(R.string.settings_backup_restore_hint))
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.FileDownload,
                            contentDescription = null,
                        )
                    },
                    modifier = Modifier.clickableIfEnabled(!isBusy, onRestoreClick),
                )
            }
            if (isBusy) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }

    if (showRestoreConfirm) {
        ConfirmDialog(
            title = stringResource(R.string.settings_backup_restore_title),
            text = stringResource(R.string.settings_backup_restore_message),
            confirmText = stringResource(R.string.settings_backup_restore),
            onConfirm = onRestoreConfirm,
            onDismiss = onRestoreDismiss,
            confirmDestructive = true,
        )
    }

    createdBackup?.let { backup ->
        BackupReadyDialog(
            onSave = { onSaveBackup(backup) },
            onShare = { onShareBackup(backup) },
            onDismiss = onDismissCreatedBackup,
        )
    }
}

@Composable
private fun BackupReadyDialog(
    onSave: () -> Unit,
    onShare: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_backup_ready_title)) },
        text = { Text(stringResource(R.string.settings_backup_ready_message)) },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text(stringResource(R.string.settings_backup_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onShare) {
                Text(stringResource(R.string.settings_backup_share))
            }
        },
    )
}

private fun Modifier.clickableIfEnabled(enabled: Boolean, onClick: () -> Unit): Modifier {
    return if (enabled) clickable(onClick = onClick) else this
}

private fun shareBackup(context: Context, backup: BackupFile) {
    val file = File(backup.path)
    val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file,
    )
    val send = Intent(Intent.ACTION_SEND).apply {
        type = BACKUP_MIME_TYPE
        clipData = ClipData.newRawUri(backup.displayName, uri)
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(
        Intent.createChooser(send, context.getString(R.string.settings_backup_share)),
    )
}

private const val BACKUP_MIME_TYPE = "application/octet-stream"
private val BACKUP_OPEN_MIME_TYPES = arrayOf(
    "application/x-sqlite3",
    "application/vnd.sqlite3",
    "application/octet-stream",
    "*/*",
)

@PreviewLightDark
@Composable
private fun SettingsContentPreview() {
    PreviewMechanicTheme {
        SettingsContent(
            isBusy = false,
            createdBackup = null,
            snackbarHostState = SnackbarHostState(),
            showRestoreConfirm = false,
            onBack = {},
            onCreateBackup = {},
            onRestoreClick = {},
            onRestoreDismiss = {},
            onRestoreConfirm = {},
            onSaveBackup = {},
            onShareBackup = {},
            onDismissCreatedBackup = {},
        )
    }
}
