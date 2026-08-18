package com.medvedev.presentation.drivers

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.medvedev.mechanic.R
import com.medvedev.presentation.components.FormField

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
