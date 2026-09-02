package com.medvedev.mechanic.presentation.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.document.DocumentAccess
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.presentation.components.MechanicTopBar
import com.medvedev.mechanic.presentation.docs.viewer.PdfPages
import com.medvedev.mechanic.presentation.error.toMessageRes
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun PdfDocumentScreen(
    onBack: () -> Unit,
    viewModel: PdfDocumentViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val title = stringResource(NormativeDocsCatalog.titleResFor(uiState.documentId))

    PdfDocumentContent(
        title = title,
        uiState = uiState,
        onBack = onBack,
        onRetry = viewModel::load,
        onConfirmDownload = viewModel::confirmDownload,
        onSkipDownload = viewModel::skipDownload,
    )
}

@Composable
private fun PdfDocumentContent(
    title: String,
    uiState: PdfDocumentUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onConfirmDownload: () -> Unit,
    onSkipDownload: () -> Unit,
) {
    val prompt = uiState.downloadRequired
    Scaffold(
        topBar = {
            MechanicTopBar(title = title, onBack = onBack)
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                uiState.error != null && uiState.error != DomainError.Storage.Full -> {
                    DocumentMessage(
                        message = stringResource(uiState.error.toMessageRes()),
                        onAction = onRetry,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                uiState.document != null -> {
                    Column(Modifier.fillMaxSize()) {
                        if (uiState.document.isOfflineCopy) {
                            Text(
                                text = stringResource(R.string.document_offline_copy),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        PdfPages(
                            path = uiState.document.path,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }

    if (prompt != null) {
        DownloadConfirmDialog(
            prompt = prompt,
            onConfirm = onConfirmDownload,
            onDismiss = {
                if (prompt.cached != null) onSkipDownload() else onBack()
            },
        )
    }

    if (uiState.error == DomainError.Storage.Full) {
        StorageFullDialog(onDismiss = onBack)
    }
}

@Composable
private fun StorageFullDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.error_storage_full_title)) },
        text = { Text(stringResource(DomainError.Storage.Full.toMessageRes())) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.got_it))
            }
        },
    )
}

@Composable
private fun DownloadConfirmDialog(
    prompt: DocumentAccess.DownloadRequired,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val isUpdate = prompt.cached != null
    val sizeLabel = prompt.sizeBytes?.let { formatDocumentSize(it) }
    val titleRes = if (isUpdate) {
        R.string.document_update_title
    } else {
        R.string.document_download_title
    }
    val message = when {
        isUpdate && sizeLabel != null ->
            stringResource(R.string.document_update_confirm, sizeLabel)

        isUpdate ->
            stringResource(R.string.document_update_confirm_unknown_size)

        sizeLabel != null ->
            stringResource(R.string.document_download_confirm, sizeLabel)

        else ->
            stringResource(R.string.document_download_confirm_unknown_size)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(titleRes)) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.download))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    stringResource(
                        if (isUpdate) R.string.document_open_saved else R.string.cancel
                    )
                )
            }
        },
    )
}

@Composable
private fun formatDocumentSize(bytes: Long): String {
    return if (bytes < BYTES_IN_MB) {
        stringResource(R.string.document_size_kb, (bytes / BYTES_IN_KB).coerceAtLeast(1).toInt())
    } else {
        stringResource(R.string.document_size_mb, bytes / BYTES_IN_MB.toDouble())
    }
}

private const val BYTES_IN_KB = 1024L
private const val BYTES_IN_MB = 1024L * 1024L

@Composable
internal fun DocumentMessage(
    message: String,
    onAction: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        if (onAction != null) {
            Button(onClick = onAction) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PdfDocumentDownloadPromptPreview() {
    PreviewMechanicTheme {
        PdfDocumentContent(
            title = "Document",
            uiState = PdfDocumentUiState(
                isLoading = false,
                downloadRequired = DocumentAccess.DownloadRequired(
                    sizeBytes = 2_400_000,
                    cached = null,
                ),
            ),
            onBack = {},
            onRetry = {},
            onConfirmDownload = {},
            onSkipDownload = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun PdfDocumentErrorPreview() {
    PreviewMechanicTheme {
        PdfDocumentContent(
            title = "Document",
            uiState = PdfDocumentUiState(
                isLoading = false,
                error = DomainError.Network.Unavailable,
            ),
            onBack = {},
            onRetry = {},
            onConfirmDownload = {},
            onSkipDownload = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun PdfDocumentStorageFullPreview() {
    PreviewMechanicTheme {
        PdfDocumentContent(
            title = "Document",
            uiState = PdfDocumentUiState(
                isLoading = false,
                error = DomainError.Storage.Full,
            ),
            onBack = {},
            onRetry = {},
            onConfirmDownload = {},
            onSkipDownload = {},
        )
    }
}
