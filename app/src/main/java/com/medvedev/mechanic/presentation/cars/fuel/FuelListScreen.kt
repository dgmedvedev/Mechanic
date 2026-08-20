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
    selectedCarId: String? = null,
    onSelectedCarIdChange: (String?) -> Unit = {},
    detailPane: @Composable (String) -> Unit = {},
    viewModel: CarListViewModel = hiltViewModel(),
) {
    CarListScreen(
        onBack = onBack,
        onNavigateToDetails = onNavigateToDetails,
        onNavigateToAdd = {},
        selectedCarId = selectedCarId,
        onSelectedCarIdChange = onSelectedCarIdChange,
        detailPane = detailPane,
        showAddButton = false,
        topBarTitle = stringResource(R.string.menu_button3),
        viewModel = viewModel,
    )
}
