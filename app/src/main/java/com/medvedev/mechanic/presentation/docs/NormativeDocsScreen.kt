package com.medvedev.mechanic.presentation.docs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.components.MechanicTopBar

@Composable
fun NormativeDocsScreen(
    onBack: () -> Unit,
    onNavigateToNorms: () -> Unit,
    onNavigateToResolution470: () -> Unit,
) {
    Scaffold(
        topBar = {
            MechanicTopBar(
                title = stringResource(R.string.menu_button4),
                onBack = onBack,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            DocMenuItem(
                title = stringResource(R.string.norms),
                onClick = onNavigateToNorms,
            )
            DocMenuItem(
                title = stringResource(R.string.resolution470),
                onClick = onNavigateToResolution470,
            )
        }
    }
}

@Composable
private fun DocMenuItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
