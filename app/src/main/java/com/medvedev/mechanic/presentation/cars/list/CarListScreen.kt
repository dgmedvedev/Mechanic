package com.medvedev.mechanic.presentation.cars.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.presentation.components.AdaptiveListDetail
import com.medvedev.mechanic.presentation.components.ExpandedListDetailBreakpoint
import com.medvedev.mechanic.presentation.components.ListContent
import com.medvedev.mechanic.presentation.components.ListPaneScaffold
import com.medvedev.mechanic.presentation.components.ListSearchField

@Composable
fun CarListScreen(
    onBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToAdd: () -> Unit,
    selectedCarId: String? = null,
    onSelectedCarIdChange: (String?) -> Unit = {},
    detailPane: @Composable (String) -> Unit = {},
    showAddButton: Boolean = true,
    topBarTitle: String = stringResource(R.string.menu_button1),
    viewModel: CarListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= ExpandedListDetailBreakpoint
        val detailId = selectedCarId.takeIf { isExpanded }

        AdaptiveListDetail(
            isExpanded = isExpanded,
            showDetail = detailId != null,
            listContent = {
                CarListPane(
                    cars = uiState.filteredItems,
                    isLoading = uiState.isLoading,
                    searchQuery = uiState.searchQuery,
                    onSearchChange = viewModel::onSearchQueryChange,
                    onBack = onBack,
                    onCarClick = { carId ->
                        if (isExpanded) {
                            onSelectedCarIdChange(carId)
                        } else {
                            onNavigateToDetails(carId)
                        }
                    },
                    onAddClick = onNavigateToAdd,
                    showAddButton = showAddButton,
                    topBarTitle = topBarTitle,
                )
            },
            detailContent = {
                detailId?.let { detailPane(it) }
            },
        )
    }
}

@Composable
private fun CarListPane(
    cars: List<Car>,
    isLoading: Boolean,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onBack: () -> Unit,
    onCarClick: (String) -> Unit,
    onAddClick: () -> Unit,
    showAddButton: Boolean,
    topBarTitle: String,
) {
    ListPaneScaffold(
        title = topBarTitle,
        onBack = onBack,
        showAddButton = showAddButton,
        onAddClick = onAddClick,
    ) {
        ListSearchField(
            query = searchQuery,
            onQueryChange = onSearchChange,
            placeholder = stringResource(R.string.search_car),
        )
        ListContent(
            items = cars,
            isLoading = isLoading,
            key = { it.id },
        ) { car ->
            CarListItem(
                car = car,
                onClick = { onCarClick(car.id) },
            )
        }
    }
}

@Composable
private fun CarListItem(car: Car, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "${car.brand} ${car.model}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(text = car.stateNumber, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = car.yearProduction.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
