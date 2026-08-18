package com.medvedev.presentation.fuel

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.medvedev.mechanic.R
import com.medvedev.presentation.cars.CarListScreen
import com.medvedev.presentation.cars.CarListViewModel

@Composable
fun FuelListScreen(
    onBack: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    selectedCarId: String? = null,
    detailContent: (@Composable () -> Unit)? = null,
    viewModel: CarListViewModel = hiltViewModel(),
) {
    CarListScreen(
        onBack = onBack,
        onNavigateToDetails = onNavigateToDetails,
        onNavigateToAdd = {},
        selectedCarId = selectedCarId,
        detailContent = detailContent,
        showAddButton = false,
        topBarTitle = stringResource(R.string.menu_button3),
        viewModel = viewModel,
    )
}
