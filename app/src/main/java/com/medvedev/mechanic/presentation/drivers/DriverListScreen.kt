package com.medvedev.mechanic.presentation.drivers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.components.AdaptiveListDetail
import com.medvedev.mechanic.presentation.components.MechanicTopBar

@Composable
fun DriverListScreen(
    onBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToAdd: () -> Unit,
    selectedDriverId: String? = null,
    detailContent: (@Composable () -> Unit)? = null,
    viewModel: DriverListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdaptiveListDetail(
        showDetail = selectedDriverId != null && detailContent != null,
        listContent = {
            DriverListPane(
                drivers = uiState.filteredItems,
                isLoading = uiState.isLoading,
                searchQuery = uiState.searchQuery,
                onSearchChange = viewModel::onSearchQueryChange,
                onBack = onBack,
                onDriverClick = onNavigateToDetails,
                onAddClick = onNavigateToAdd,
            )
        },
        detailContent = { detailContent?.invoke() },
    )
}

@Composable
private fun DriverListPane(
    drivers: List<Driver>,
    isLoading: Boolean,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onBack: () -> Unit,
    onDriverClick: (String) -> Unit,
    onAddClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            MechanicTopBar(
                title = stringResource(R.string.menu_button2),
                onBack = onBack,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp),
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text(stringResource(R.string.search_driver)) },
                singleLine = true,
            )
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(drivers, key = { it.id }) { driver ->
                        DriverListItem(driver = driver, onClick = { onDriverClick(driver.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun DriverListItem(driver: Driver, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "${driver.surname} ${driver.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${stringResource(R.string.driving_license_validity)}: ${driver.drivingLicenseValidity}",
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = "${stringResource(R.string.medical_certificate_validity)}: ${driver.medicalCertificateValidity}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
