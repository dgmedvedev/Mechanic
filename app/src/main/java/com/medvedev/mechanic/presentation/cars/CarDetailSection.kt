package com.medvedev.mechanic.presentation.cars

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R

enum class CarDetailSection {
    DATA,
    FUEL_RATES,
}

@Composable
fun CarDetailSectionSelector(
    selected: CarDetailSection,
    onSelected: (CarDetailSection) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(
        CarDetailSection.DATA to stringResource(R.string.car_section_data),
        CarDetailSection.FUEL_RATES to stringResource(R.string.car_section_nrt),
    )
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        options.forEachIndexed { index, (section, label) ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = { onSelected(section) },
                selected = section == selected,
                icon = {},
                label = { Text(label) },
            )
        }
    }
}
