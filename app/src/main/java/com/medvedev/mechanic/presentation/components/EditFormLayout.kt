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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditFormLayout(
    title: String,
    onClose: () -> Unit,
    embedded: Boolean,
    isLoading: Boolean,
    isSaving: Boolean,
    snackbarHostState: SnackbarHostState,
    onSave: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (embedded) {
        BackHandler(onBack = onClose)
    }

    Scaffold(
        topBar = {
            if (!embedded) {
                MechanicTopBar(title = title, onBack = onClose)
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
                    showClose = embedded,
                    onCloseClick = onClose,
                    onSaveClick = onSave,
                    saveEnabled = !isSaving,
                    content = content,
                )
            }
        }
    }
}
