package com.medvedev.mechanic.presentation.drivers.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.components.FormField
import com.medvedev.mechanic.presentation.preview.PreviewDriver
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
internal fun DriverFormFields(
    form: DriverFormState,
    onFormChange: ((DriverFormState) -> DriverFormState) -> Unit,
) {
    FormField(
        label = stringResource(R.string.surname),
        value = form.surname,
        onValueChange = { value -> onFormChange { it.copy(surname = value) } },
    )
    FormField(
        label = stringResource(R.string.name),
        value = form.name,
        onValueChange = { value -> onFormChange { it.copy(name = value) } },
    )
    FormField(
        label = stringResource(R.string.middle_name),
        value = form.middleName,
        onValueChange = { value -> onFormChange { it.copy(middleName = value) } },
    )
    FormField(
        label = stringResource(R.string.birthday),
        value = form.birthday,
        onValueChange = { value -> onFormChange { it.copy(birthday = value) } },
    )
    FormField(
        label = stringResource(R.string.driving_license_number),
        value = form.drivingLicenseNumber,
        onValueChange = { value -> onFormChange { it.copy(drivingLicenseNumber = value) } },
    )
    FormField(
        label = stringResource(R.string.driving_license_validity),
        value = form.drivingLicenseValidity,
        onValueChange = { value -> onFormChange { it.copy(drivingLicenseValidity = value) } },
    )
    FormField(
        label = stringResource(R.string.medical_certificate_validity),
        value = form.medicalCertificateValidity,
        onValueChange = { value -> onFormChange { it.copy(medicalCertificateValidity = value) } },
    )
}

@Preview(showBackground = true)
@Composable
private fun DriverFormFieldsPreview() {
    PreviewMechanicTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DriverFormFields(
                form = DriverFormState.fromDriver(PreviewDriver),
                onFormChange = {},
            )
        }
    }
}
