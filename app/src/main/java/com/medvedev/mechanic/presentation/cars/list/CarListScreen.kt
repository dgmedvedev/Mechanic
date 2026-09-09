package com.medvedev.mechanic.presentation.cars.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.presentation.cars.CarDetailSection
import com.medvedev.mechanic.presentation.cars.detail.CarDetailsContent
import com.medvedev.mechanic.presentation.components.DetailPaneHost
import com.medvedev.mechanic.presentation.components.AdaptiveListDetail
import com.medvedev.mechanic.presentation.components.CompactDetailScaffold
import com.medvedev.mechanic.presentation.components.ListContent
import com.medvedev.mechanic.presentation.components.ListEntityItem
import com.medvedev.mechanic.presentation.components.ListPaneScaffold
import com.medvedev.mechanic.presentation.components.ListSearchField
import com.medvedev.mechanic.presentation.components.expandedListDetailBreakpoint
import com.medvedev.mechanic.presentation.components.explicitDetailId
import com.medvedev.mechanic.presentation.components.rememberListDetailPaneState
import com.medvedev.mechanic.presentation.components.resolveDetailId
import com.medvedev.mechanic.presentation.preview.PreviewCar
import com.medvedev.mechanic.presentation.preview.PreviewCars
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun CarListScreen(
    detailContent: @Composable (carId: String, onEdit: () -> Unit, onDeleted: () -> Unit) -> Unit = { _, _, _ -> },
    editContent: @Composable (carId: String?, embedded: Boolean, onClose: () -> Unit) -> Unit = { _, _, _ -> },
    viewModel: CarListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val paneState = rememberListDetailPaneState()
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= expandedListDetailBreakpoint
        val visibleIds = uiState.filteredItems.map { it.id }
        val compactDetailId = paneState.explicitDetailId(uiState.isLoading, visibleIds)
        val detailId = if (isExpanded) {
            if (uiState.isLoading) paneState.selectedId else resolveDetailId(
                paneState.selectedId,
                visibleIds
            )
        } else {
            compactDetailId
        }
        val showCompactDetail = !isExpanded &&
                (paneState.isAdding || paneState.editingId != null || compactDetailId != null)
        val detailEmptyMessage = if (uiState.items.isEmpty()) {
            stringResource(R.string.detail_empty_car)
        } else {
            stringResource(R.string.detail_empty_search)
        }

        BackHandler(
            enabled = showCompactDetail && !paneState.isAdding && paneState.editingId == null,
            onBack = paneState::clear,
        )

        AdaptiveListDetail(
            isExpanded = isExpanded,
            showCompactDetail = showCompactDetail,
            listContent = {
                CarListPane(
                    cars = uiState.filteredItems,
                    isLoading = uiState.isLoading,
                    searchQuery = uiState.searchQuery,
                    selectedCarId = if (paneState.isAdding) null else detailId,
                    onSearchChange = viewModel::onSearchQueryChange,
                    onCarClick = paneState::select,
                    onAddClick = paneState::startAdding,
                    topBarTitle = stringResource(R.string.cars),
                    listState = listState,
                )
            },
            detailContent = {
                if (paneState.isAdding) {
                    editContent(null, isExpanded, paneState::stopAdding)
                } else {
                    DetailPaneHost(
                        isLoading = uiState.isLoading,
                        detailId = detailId,
                        emptyMessage = detailEmptyMessage,
                        content = { carId ->
                            if (paneState.editingId == carId) {
                                editContent(carId, isExpanded, paneState::stopEditing)
                            } else if (isExpanded) {
                                detailContent(
                                    carId,
                                    { paneState.startEditing(carId) },
                                    paneState::clear,
                                )
                            } else {
                                CompactDetailScaffold(
                                    title = stringResource(R.string.cars),
                                    onBack = paneState::clear,
                                ) {
                                    detailContent(
                                        carId,
                                        { paneState.startEditing(carId) },
                                        paneState::clear,
                                    )
                                }
                            }
                        },
                    )
                }
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
    onCarClick: (String) -> Unit,
    onAddClick: () -> Unit,
    topBarTitle: String,
    listState: LazyListState = rememberLazyListState(),
) {
    ListPaneScaffold(
        title = topBarTitle,
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
            listState = listState,
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
                    onCarClick = {},
                    onAddClick = {},
                    topBarTitle = "Автомобили",
                )
            },
            detailContent = {
                CarDetailsContent(
                    car = PreviewCar,
                    section = CarDetailSection.DATA,
                    onEditClick = {},
                    onDeleteClick = {},
                )
            },
        )
    }
}
