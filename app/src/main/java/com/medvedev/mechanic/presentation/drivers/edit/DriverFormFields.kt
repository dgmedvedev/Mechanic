package com.medvedev.mechanic.presentation.drivers.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.components.DetailRow
import com.medvedev.mechanic.presentation.preview.PreviewDriver
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
internal fun DriverFormFields(
    form: DriverFormState,
    onFormChange: ((DriverFormState) -> DriverFormState) -> Unit,
) {
    DetailRow(
        label = stringResource(R.string.surname),
        value = form.surname,
        icon = Icons.Outlined.Person,
        onValueChange = { value -> onFormChange { it.copy(surname = value) } },
    )
    DetailRow(
        label = stringResource(R.string.name),
        value = form.name,
        icon = Icons.Outlined.Person,
        onValueChange = { value -> onFormChange { it.copy(name = value) } },
    )
    DetailRow(
        label = stringResource(R.string.middle_name),
        value = form.middleName,
        icon = Icons.Outlined.PersonOutline,
        onValueChange = { value -> onFormChange { it.copy(middleName = value) } },
    )
    DetailRow(
        label = stringResource(R.string.birthday),
        value = form.birthday,
        icon = Icons.Outlined.Cake,
        onValueChange = { value -> onFormChange { it.copy(birthday = value) } },
    )
    DetailRow(
        label = stringResource(R.string.driving_license_number),
        value = form.drivingLicenseNumber,
        icon = Icons.Outlined.Badge,
        onValueChange = { value -> onFormChange { it.copy(drivingLicenseNumber = value) } },
    )
    DetailRow(
        label = stringResource(R.string.driving_license_validity),
        value = form.drivingLicenseValidity,
        icon = Icons.Outlined.Event,
        onValueChange = { value -> onFormChange { it.copy(drivingLicenseValidity = value) } },
    )
    DetailRow(
        label = stringResource(R.string.medical_certificate_validity),
        value = form.medicalCertificateValidity,
        icon = Icons.Outlined.MedicalServices,
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
