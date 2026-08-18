package com.medvedev.mechanic.presentation.cars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.components.MechanicTopBar

@Composable
fun CarEditScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: CarEditViewModel = hiltViewModel(),
) {
    val resources = LocalResources.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val imageUrl = stringResource(R.string.image_url)
    val defaultBrand = stringResource(R.string.brand)
    val defaultModel = stringResource(R.string.model)

    LaunchedEffect(uiState.saveCompleted) {
        if (uiState.saveCompleted) {
            viewModel.consumeSaveCompleted()
            onSaved()
        }
    }

    LaunchedEffect(uiState.errorMessageRes) {
        uiState.errorMessageRes?.let { messageRes ->
            snackbarHostState.showSnackbar(resources.getString(messageRes))
            viewModel.consumeError()
        }
    }

    Scaffold(
        topBar = {
            MechanicTopBar(
                title = stringResource(R.string.menu_button1),
                onBack = onBack,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                CarFormFields(
                    form = uiState.form,
                    onFormChange = viewModel::onFormChange,
                )
                Button(
                    onClick = {
                        viewModel.saveCar(
                            imageUrl = imageUrl,
                            defaultBrand = defaultBrand,
                            defaultModel = defaultModel,
                        )
                    },
                    enabled = !uiState.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}
