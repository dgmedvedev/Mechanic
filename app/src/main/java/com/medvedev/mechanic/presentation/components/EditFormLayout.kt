package com.medvedev.mechanic.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R

@Composable
fun EditFormLayout(
    title: String,
    onClose: () -> Unit,
    embedded: Boolean,
    isLoading: Boolean,
    isSaving: Boolean,
    snackbarHostState: SnackbarHostState,
    onSave: () -> Unit,
    isDirty: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val showDiscardConfirm = rememberSaveable { mutableStateOf(false) }
    val currentIsDirty by rememberUpdatedState(isDirty)
    val currentOnClose by rememberUpdatedState(onClose)
    val requestClose = {
        if (currentIsDirty) {
            showDiscardConfirm.value = true
        } else {
            currentOnClose()
        }
    }

    BackHandler(onBack = requestClose)

    Scaffold(
        topBar = {
            if (!embedded) {
                MechanicTopBar(title = title, onBack = requestClose)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = if (embedded) {
            WindowInsets(0.dp)
        } else {
            ScaffoldDefaults.contentWindowInsets
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
        ) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else {
                DetailContentLayout(
                    editing = true,
                    showClose = true,
                    onCloseClick = requestClose,
                    onSaveClick = onSave,
                    saveEnabled = !isSaving,
                    content = content,
                )
            }
        }
    }

    if (showDiscardConfirm.value) {
        ConfirmDialog(
            title = stringResource(R.string.discard_changes_title),
            text = stringResource(R.string.discard_changes_message),
            confirmText = stringResource(R.string.close),
            onConfirm = currentOnClose,
            onDismiss = { showDiscardConfirm.value = false },
        )
    }
}
