package com.medvedev.presentation.cars

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.medvedev.mechanic.R
import com.medvedev.presentation.components.FormField

@Composable
internal fun CarFormFields(
    form: CarFormState,
    onFormChange: ((CarFormState) -> CarFormState) -> Unit,
    fuelOnly: Boolean = false,
) {
    FormField(
        label = stringResource(R.string.brand),
        value = form.brand,
        onValueChange = { value -> onFormChange { it.copy(brand = value) } },
    )
    FormField(
        label = stringResource(R.string.model),
        value = form.model,
        onValueChange = { value -> onFormChange { it.copy(model = value) } },
    )
    FormField(
        label = stringResource(R.string.year_production),
        value = form.yearProduction,
        onValueChange = { value -> onFormChange { it.copy(yearProduction = value) } },
    )
    FormField(
        label = stringResource(R.string.state_number),
        value = form.stateNumber,
        onValueChange = { value -> onFormChange { it.copy(stateNumber = value) } },
    )

    if (!fuelOnly) {
        FormField(
            label = stringResource(R.string.body_number),
            value = form.bodyNumber,
            onValueChange = { value -> onFormChange { it.copy(bodyNumber = value) } },
        )
        FormField(
            label = stringResource(R.string.engine_displacement),
            value = form.engineDisplacement,
            onValueChange = { value -> onFormChange { it.copy(engineDisplacement = value) } },
        )
        FormField(
            label = stringResource(R.string.fuel_type),
            value = form.fuelType,
            onValueChange = { value -> onFormChange { it.copy(fuelType = value) } },
        )
        FormField(
            label = stringResource(R.string.allowable_weight),
            value = form.allowableWeight,
            onValueChange = { value -> onFormChange { it.copy(allowableWeight = value) } },
        )
        FormField(
            label = stringResource(R.string.technical_passport),
            value = form.technicalPassport,
            onValueChange = { value -> onFormChange { it.copy(technicalPassport = value) } },
        )
        FormField(
            label = stringResource(R.string.checkup),
            value = form.checkup,
            onValueChange = { value -> onFormChange { it.copy(checkup = value) } },
        )
        FormField(
            label = stringResource(R.string.insurance),
            value = form.insurance,
            onValueChange = { value -> onFormChange { it.copy(insurance = value) } },
        )
        FormField(
            label = stringResource(R.string.hull_insurance),
            value = form.hullInsurance,
            onValueChange = { value -> onFormChange { it.copy(hullInsurance = value) } },
        )
    } else {
        FormField(
            label = stringResource(R.string.linear_fcr),
            value = form.linearFcr,
            onValueChange = { value -> onFormChange { it.copy(linearFcr = value) } },
        )
        FormField(
            label = "${stringResource(R.string.summer_fcr)} ${stringResource(R.string.in_the_city)}",
            value = form.summerInCityFcr,
            onValueChange = { value -> onFormChange { it.copy(summerInCityFcr = value) } },
        )
        FormField(
            label = "${stringResource(R.string.summer_fcr)} ${stringResource(R.string.outside_the_city)}",
            value = form.summerOutCityFcr,
            onValueChange = { value -> onFormChange { it.copy(summerOutCityFcr = value) } },
        )
        FormField(
            label = "${stringResource(R.string.winter_fcr)} ${stringResource(R.string.in_the_city)}",
            value = form.winterInCityFcr,
            onValueChange = { value -> onFormChange { it.copy(winterInCityFcr = value) } },
        )
        FormField(
            label = "${stringResource(R.string.winter_fcr)} ${stringResource(R.string.outside_the_city)}",
            value = form.winterOutCityFcr,
            onValueChange = { value -> onFormChange { it.copy(winterOutCityFcr = value) } },
        )
    }
}
