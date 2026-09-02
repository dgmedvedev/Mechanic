package com.medvedev.mechanic.presentation.docs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.components.MechanicTopBar
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun NormativeDocsScreen(
    onBack: () -> Unit,
    onNavigateToDocument: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            MechanicTopBar(
                title = stringResource(R.string.menu_button4),
                onBack = onBack,
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = NormativeDocsCatalog.items,
                key = { it.id },
            ) { item ->
                DocMenuItem(
                    title = stringResource(item.titleRes),
                    onClick = { onNavigateToDocument(item.id) },
                )
            }
        }
    }
}

@Composable
private fun DocMenuItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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

@PreviewLightDark
@Composable
private fun NormativeDocsScreenPreview() {
    PreviewMechanicTheme {
        NormativeDocsScreen(
            onBack = {},
            onNavigateToDocument = {},
        )
    }
}
