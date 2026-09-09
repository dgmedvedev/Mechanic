package com.medvedev.mechanic.presentation.drivers.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
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
import com.medvedev.mechanic.domain.model.Driver
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
import com.medvedev.mechanic.presentation.preview.PreviewDriver
import com.medvedev.mechanic.presentation.preview.PreviewDrivers
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun DriverListScreen(
    detailContent: @Composable (driverId: String, onEdit: () -> Unit, onDeleted: () -> Unit) -> Unit = { _, _, _ -> },
    editContent: @Composable (driverId: String?, embedded: Boolean, onClose: () -> Unit) -> Unit = { _, _, _ -> },
    viewModel: DriverListViewModel = hiltViewModel(),
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
            stringResource(R.string.detail_empty_driver)
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
                DriverListPane(
                    drivers = uiState.filteredItems,
                    isLoading = uiState.isLoading,
                    searchQuery = uiState.searchQuery,
                    selectedDriverId = if (paneState.isAdding) null else detailId,
                    onSearchChange = viewModel::onSearchQueryChange,
                    onDriverClick = paneState::select,
                    onAddClick = paneState::startAdding,
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
                        content = { driverId ->
                            if (paneState.editingId == driverId) {
                                editContent(driverId, isExpanded, paneState::stopEditing)
                            } else if (isExpanded) {
                                detailContent(
                                    driverId,
                                    { paneState.startEditing(driverId) },
                                    paneState::clear,
                                )
                            } else {
                                CompactDetailScaffold(
                                    title = stringResource(R.string.drivers),
                                    onBack = paneState::clear,
                                ) {
                                    detailContent(
                                        driverId,
                                        { paneState.startEditing(driverId) },
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
private fun DriverListPane(
    drivers: List<Driver>,
    isLoading: Boolean,
    searchQuery: String,
    selectedDriverId: String?,
    onSearchChange: (String) -> Unit,
    onDriverClick: (String) -> Unit,
    onAddClick: () -> Unit,
    listState: LazyListState = rememberLazyListState(),
) {
    ListPaneScaffold(
        title = stringResource(R.string.drivers),
        onAddClick = onAddClick,
    ) {
        ListSearchField(
            query = searchQuery,
            onQueryChange = onSearchChange,
            placeholder = stringResource(R.string.search_driver),
        )
        ListContent(
            items = drivers,
            isLoading = isLoading,
            key = { it.id },
            footer = if (isLoading) {
                null
            } else {
                pluralStringResource(R.plurals.total_drivers, drivers.size, drivers.size)
            },
            listState = listState,
        ) { driver ->
            DriverListItem(
                driver = driver,
                selected = driver.id == selectedDriverId,
                onClick = { onDriverClick(driver.id) },
            )
        }
    }
}

@Composable
private fun DriverListItem(
    driver: Driver,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val subtitle = listOf(driver.birthday, driver.drivingLicenseNumber)
        .filter { it.isNotBlank() }
        .joinToString(" • ")

    ListEntityItem(
        title = "${driver.surname} ${driver.name}".trim(),
        subtitle = subtitle,
        placeholderIcon = Icons.Outlined.Person,
        selected = selected,
        onClick = onClick,
        imageContentDescription = "${driver.surname} ${driver.name}",
    )
}

@PreviewLightDark
@Composable
private fun DriverListPanePreview() {
    PreviewMechanicTheme {
        DriverListPane(
            drivers = PreviewDrivers,
            isLoading = false,
            searchQuery = "",
            selectedDriverId = PreviewDriver.id,
            onSearchChange = {},
            onDriverClick = {},
            onAddClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DriverListItemPreview() {
    PreviewMechanicTheme {
        DriverListItem(
            driver = PreviewDriver,
            selected = true,
            onClick = {},
        )
    }
}
