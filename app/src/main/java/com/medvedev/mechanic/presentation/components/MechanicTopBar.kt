package com.medvedev.mechanic.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MechanicTopBar(
    title: String,
    showBack: Boolean = true,
    onBack: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun MechanicTopBarPreview() {
    PreviewMechanicTheme {
        MechanicTopBar(title = "Автомобили", onBack = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun MechanicTopBarNoBackPreview() {
    PreviewMechanicTheme {
        MechanicTopBar(title = "mechanic", showBack = false)
    }
}
