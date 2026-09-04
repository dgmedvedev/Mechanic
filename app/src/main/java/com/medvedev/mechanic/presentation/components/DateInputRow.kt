package com.medvedev.mechanic.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CarRepair
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val DATE_PATTERN = "dd.MM.yyyy"
private val UtcTimeZone: TimeZone = TimeZone.getTimeZone("UTC")

@Composable
fun DateInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    DetailRow(
        label = label,
        value = value,
        modifier = modifier,
        icon = icon,
        onClick = { showDialog.value = true },
    )

    if (showDialog.value) {
        DateInputDialog(
            label = label,
            initialValue = value,
            onDismiss = { showDialog.value = false },
            onConfirm = onValueChange,
        )
    }
}

@Composable
private fun DateInputDialog(
    label: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = parseDateMillis(initialValue),
        initialDisplayMode = DisplayMode.Input,
    )
    val confirmEnabled by remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onConfirm(formatDateMillis(millis))
                        onDismiss()
                    }
                },
                enabled = confirmEnabled,
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    ) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier.verticalScroll(rememberScrollState()),
            title = {
                Text(
                    text = label,
                    modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
                )
            },
        )
    }
}

private fun parseDateMillis(value: String): Long? {
    if (value.isBlank()) return null
    return runCatching { dateFormat().parse(value)?.time }.getOrNull()
}

private fun formatDateMillis(millis: Long): String = dateFormat().format(Date(millis))

private fun dateFormat(): SimpleDateFormat =
    SimpleDateFormat(DATE_PATTERN, Locale.US).apply {
        timeZone = UtcTimeZone
        isLenient = false
    }

@Preview(showBackground = true)
@Composable
private fun DateInputRowPreview() {
    PreviewMechanicTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DateInputRow(
                label = "Техосмотр",
                value = "01.06.2027",
                icon = Icons.Outlined.CarRepair,
                onValueChange = {},
            )
            DateInputRow(
                label = "ОСАГО",
                value = "",
                icon = Icons.Outlined.Shield,
                onValueChange = {},
            )
        }
    }
}
