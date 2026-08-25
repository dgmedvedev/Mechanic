package com.medvedev.mechanic.presentation.cars.fuel

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.cars.list.CarListScreen
import com.medvedev.mechanic.presentation.cars.list.CarListViewModel

@Composable
fun FuelListScreen(
    onBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    detailContent: @Composable (carId: String, onEdit: () -> Unit, onDeleted: () -> Unit) -> Unit = { _, _, _ -> },
    editContent: @Composable (carId: String, onClose: () -> Unit) -> Unit = { _, _ -> },
    viewModel: CarListViewModel = hiltViewModel(),
) {
    CarListScreen(
        onBack = onBack,
        onNavigateToDetails = onNavigateToDetails,
        onNavigateToAdd = {},
        detailContent = detailContent,
        editContent = editContent,
        showAddButton = false,
        topBarTitle = stringResource(R.string.menu_button3),
        emptyDetailMessage = stringResource(R.string.detail_empty_fuel),
        viewModel = viewModel,
    )
}
