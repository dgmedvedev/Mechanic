package com.medvedev.mechanic.presentation.cars.list

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.presentation.components.AdaptiveDetailPane
import com.medvedev.mechanic.presentation.components.AdaptiveListDetail
import com.medvedev.mechanic.presentation.components.ExpandedListDetailBreakpoint
import com.medvedev.mechanic.presentation.components.ListContent
import com.medvedev.mechanic.presentation.components.ListEntityItem
import com.medvedev.mechanic.presentation.components.ListPaneScaffold
import com.medvedev.mechanic.presentation.components.ListSearchField
import com.medvedev.mechanic.presentation.components.rememberListDetailPaneState
import com.medvedev.mechanic.presentation.components.resolveDetailId
import com.medvedev.mechanic.presentation.cars.detail.CarDetailsContent
import com.medvedev.mechanic.presentation.preview.PreviewCar
import com.medvedev.mechanic.presentation.preview.PreviewCars
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun CarListScreen(
    onBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onNavigateToAdd: () -> Unit,
    detailContent: @Composable (carId: String, onEdit: () -> Unit, onDeleted: () -> Unit) -> Unit = { _, _, _ -> },
    editContent: @Composable (carId: String, onClose: () -> Unit) -> Unit = { _, _ -> },
    showAddButton: Boolean = true,
    topBarTitle: String = stringResource(R.string.menu_button1),
    emptyDetailMessage: String = stringResource(R.string.detail_empty_car),
    viewModel: CarListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val paneState = rememberListDetailPaneState()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= ExpandedListDetailBreakpoint
        val detailId = when {
            !isExpanded -> null
            uiState.isLoading -> paneState.selectedId
            else -> resolveDetailId(paneState.selectedId, uiState.filteredItems.map { it.id })
        }
        val detailEmptyMessage = if (uiState.items.isEmpty()) {
            emptyDetailMessage
        } else {
            stringResource(R.string.detail_empty_search)
        }

        AdaptiveListDetail(
            isExpanded = isExpanded,
            listContent = {
                CarListPane(
                    cars = uiState.filteredItems,
                    isLoading = uiState.isLoading,
                    searchQuery = uiState.searchQuery,
                    selectedCarId = detailId,
                    onSearchChange = viewModel::onSearchQueryChange,
                    onBack = onBack,
                    onCarClick = { carId ->
                        if (isExpanded) {
                            paneState.select(carId)
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
                AdaptiveDetailPane(
                    isLoading = uiState.isLoading,
                    detailId = detailId,
                    emptyMessage = detailEmptyMessage,
                    content = { carId ->
                        if (paneState.editingId == carId) {
                            editContent(carId, paneState::stopEditing)
                        } else {
                            detailContent(
                                carId,
                                { paneState.startEditing(carId) },
                                paneState::clear,
                            )
                        }
                    },
                )
            },
        )
    }
}

@Composable
private fun CarListPane(
    cars: List<Car>,
    isLoading: Boolean,
    searchQuery: String,
    selectedCarId: String?,
    onSearchChange: (String) -> Unit,
    onBack: () -> Unit,
    onCarClick: (String) -> Unit,
    onAddClick: () -> Unit,
    topBarTitle: String,
    showAddButton: Boolean = true,
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
            footer = if (isLoading) {
                null
            } else {
                pluralStringResource(R.plurals.total_cars, cars.size, cars.size)
            },
        ) { car ->
            CarListItem(
                car = car,
                selected = car.id == selectedCarId,
                onClick = { onCarClick(car.id) },
            )
        }
    }
}

@Composable
private fun CarListItem(
    car: Car,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val subtitle = listOf(car.yearProduction.toString(), car.stateNumber)
        .filter { it.isNotBlank() }
        .joinToString(" • ")
        .ifBlank { car.vin }

    ListEntityItem(
        title = "${car.brand} ${car.model}".trim(),
        subtitle = subtitle,
        placeholderIcon = Icons.Filled.DirectionsCar,
        selected = selected,
        onClick = onClick,
        imageContentDescription = stringResource(R.string.car_logo),
    )
}

@PreviewLightDark
@Composable
private fun CarListPanePreview() {
    PreviewMechanicTheme {
        CarListPane(
            cars = PreviewCars,
            isLoading = false,
            searchQuery = "",
            selectedCarId = PreviewCar.id,
            onSearchChange = {},
            onBack = {},
            onCarClick = {},
            onAddClick = {},
            topBarTitle = "Автомобили",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CarListItemPreview() {
    PreviewMechanicTheme {
        CarListItem(
            car = PreviewCar,
            selected = true,
            onClick = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 900, heightDp = 560)
@Composable
private fun CarListDetailPreview() {
    PreviewMechanicTheme {
        AdaptiveListDetail(
            isExpanded = true,
            listContent = {
                CarListPane(
                    cars = PreviewCars,
                    isLoading = false,
                    searchQuery = "",
                    selectedCarId = PreviewCar.id,
                    onSearchChange = {},
                    onBack = {},
                    onCarClick = {},
                    onAddClick = {},
                    topBarTitle = "Автомобили",
                )
            },
            detailContent = {
                CarDetailsContent(
                    car = PreviewCar,
                    onEditClick = {},
                    onDeleteClick = {},
                )
            },
        )
    }
}
