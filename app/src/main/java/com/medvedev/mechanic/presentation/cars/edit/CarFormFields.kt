package com.medvedev.mechanic.presentation.cars.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CarRepair
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.cars.CarDetailSection
import com.medvedev.mechanic.presentation.cars.CarDetailSectionSelector
import com.medvedev.mechanic.presentation.components.DateInputRow
import com.medvedev.mechanic.presentation.components.DetailInputType
import com.medvedev.mechanic.presentation.components.DetailRow
import com.medvedev.mechanic.presentation.preview.PreviewCar
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
internal fun CarFormFields(
    form: CarFormState,
    onFormChange: ((CarFormState) -> CarFormState) -> Unit,
    section: CarDetailSection,
    onSectionChange: (CarDetailSection) -> Unit,
    showFieldErrors: Boolean = false,
) {
    CarIdentityFields(
        form = form,
        onFormChange = onFormChange,
        showFieldErrors = showFieldErrors,
    )
    CarDetailSectionSelector(
        selected = section,
        onSelected = onSectionChange,
    )
    when (section) {
        CarDetailSection.DATA -> CarDataFields(form = form, onFormChange = onFormChange)
        CarDetailSection.FUEL_RATES -> CarFuelRateFields(form = form, onFormChange = onFormChange)
    }
}

@Composable
private fun CarIdentityFields(
    form: CarFormState,
    onFormChange: ((CarFormState) -> CarFormState) -> Unit,
    showFieldErrors: Boolean,
) {
    DetailRow(
        label = stringResource(R.string.required_label, stringResource(R.string.brand)),
        value = form.brand,
        icon = Icons.Outlined.DirectionsCar,
        inputType = DetailInputType.CapitalizeFirst,
        isError = showFieldErrors && !form.isBrandValid(),
        onValueChange = { value -> onFormChange { it.copy(brand = value) } },
    )
    DetailRow(
        label = stringResource(R.string.required_label, stringResource(R.string.model)),
        value = form.model,
        icon = Icons.Outlined.DirectionsCar,
        inputType = DetailInputType.CapitalizeFirst,
        isError = showFieldErrors && !form.isModelValid(),
        onValueChange = { value -> onFormChange { it.copy(model = value) } },
    )
    DetailRow(
        label = stringResource(R.string.required_label, stringResource(R.string.year_production)),
        value = form.yearProduction,
        icon = Icons.Outlined.CalendarMonth,
        inputType = DetailInputType.Year,
        isError = showFieldErrors && !form.isYearValid(),
        onValueChange = { value -> onFormChange { it.copy(yearProduction = value) } },
    )
    DetailRow(
        label = stringResource(R.string.state_number),
        value = form.stateNumber,
        icon = Icons.Outlined.Pin,
        inputType = DetailInputType.Uppercase,
        onValueChange = { value -> onFormChange { it.copy(stateNumber = value) } },
    )
}

@Composable
private fun CarDataFields(
    form: CarFormState,
    onFormChange: ((CarFormState) -> CarFormState) -> Unit,
) {
    DetailRow(
        label = stringResource(R.string.vin),
        value = form.vin,
        icon = Icons.Outlined.Pin,
        inputType = DetailInputType.Uppercase,
        onValueChange = { value -> onFormChange { it.copy(vin = value) } },
    )
    DetailRow(
        label = stringResource(R.string.engine_displacement),
        value = form.engineDisplacement,
        icon = Icons.Outlined.Speed,
        inputType = DetailInputType.Decimal,
        onValueChange = { value -> onFormChange { it.copy(engineDisplacement = value) } },
    )
    DetailRow(
        label = stringResource(R.string.fuel_type),
        value = form.fuelType,
        icon = Icons.Outlined.LocalGasStation,
        onValueChange = { value -> onFormChange { it.copy(fuelType = value) } },
    )
    DetailRow(
        label = stringResource(R.string.allowable_weight),
        value = form.allowableWeight,
        icon = Icons.Outlined.Scale,
        inputType = DetailInputType.Decimal,
        onValueChange = { value -> onFormChange { it.copy(allowableWeight = value) } },
    )
    DetailRow(
        label = stringResource(R.string.technical_passport),
        value = form.technicalPassport,
        icon = Icons.Outlined.Badge,
        inputType = DetailInputType.Uppercase,
        onValueChange = { value -> onFormChange { it.copy(technicalPassport = value) } },
    )
    DateInputRow(
        label = stringResource(R.string.checkup),
        value = form.checkup,
        icon = Icons.Outlined.CarRepair,
        onValueChange = { value -> onFormChange { it.copy(checkup = value) } },
    )
    DateInputRow(
        label = stringResource(R.string.insurance),
        value = form.insurance,
        icon = Icons.Outlined.Shield,
        onValueChange = { value -> onFormChange { it.copy(insurance = value) } },
    )
    DateInputRow(
        label = stringResource(R.string.hull_insurance),
        value = form.hullInsurance,
        icon = Icons.Outlined.HealthAndSafety,
        onValueChange = { value -> onFormChange { it.copy(hullInsurance = value) } },
    )
}

@Composable
private fun CarFuelRateFields(
    form: CarFormState,
    onFormChange: ((CarFormState) -> CarFormState) -> Unit,
) {
    DetailRow(
        label = stringResource(R.string.linear_fcr),
        value = form.linearFcr,
        icon = Icons.Outlined.Speed,
        inputType = DetailInputType.FuelRate,
        onValueChange = { value -> onFormChange { it.copy(linearFcr = value) } },
    )
    DetailRow(
        label = "${stringResource(R.string.summer_fcr)}\n${stringResource(R.string.in_the_city)}",
        value = form.summerInCityFcr,
        icon = Icons.Outlined.WbSunny,
        inputType = DetailInputType.FuelRate,
        onValueChange = { value -> onFormChange { it.copy(summerInCityFcr = value) } },
    )
    DetailRow(
        label = "${stringResource(R.string.summer_fcr)}\n${stringResource(R.string.outside_the_city)}",
        value = form.summerOutCityFcr,
        icon = Icons.Outlined.WbSunny,
        inputType = DetailInputType.FuelRate,
        onValueChange = { value -> onFormChange { it.copy(summerOutCityFcr = value) } },
    )
    DetailRow(
        label = "${stringResource(R.string.winter_fcr)}\n${stringResource(R.string.in_the_city)}",
        value = form.winterInCityFcr,
        icon = Icons.Outlined.AcUnit,
        inputType = DetailInputType.FuelRate,
        onValueChange = { value -> onFormChange { it.copy(winterInCityFcr = value) } },
    )
    DetailRow(
        label = "${stringResource(R.string.winter_fcr)}\n${stringResource(R.string.outside_the_city)}",
        value = form.winterOutCityFcr,
        icon = Icons.Outlined.AcUnit,
        inputType = DetailInputType.FuelRate,
        onValueChange = { value -> onFormChange { it.copy(winterOutCityFcr = value) } },
    )
}

@Preview(showBackground = true)
@Composable
private fun CarFormFieldsPreview() {
    PreviewMechanicTheme {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            CarFormFields(
                form = CarFormState.fromCar(PreviewCar),
                onFormChange = {},
                section = CarDetailSection.DATA,
                onSectionChange = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CarFormFieldsErrorPreview() {
    PreviewMechanicTheme {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            CarFormFields(
                form = CarFormState(),
                onFormChange = {},
                section = CarDetailSection.DATA,
                onSectionChange = {},
                showFieldErrors = true,
            )
        }
    }
}
