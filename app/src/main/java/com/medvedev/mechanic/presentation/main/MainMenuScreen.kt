package com.medvedev.mechanic.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.components.MechanicTopBar
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun MainMenuScreen(
    onNavigateToCars: () -> Unit,
    onNavigateToDrivers: () -> Unit,
    onNavigateToFuel: () -> Unit,
    onNavigateToDocs: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MechanicTopBar(
                title = stringResource(R.string.app_name),
                showBack = false,
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MenuRow(
                modifier = Modifier.weight(1f),
                MenuItem(
                    stringResource(R.string.menu_button1),
                    Icons.Default.DirectionsCar,
                    onNavigateToCars
                ),
                MenuItem(
                    stringResource(R.string.menu_button2),
                    Icons.Default.People,
                    onNavigateToDrivers
                ),
            )
            MenuRow(
                modifier = Modifier.weight(1f),
                MenuItem(
                    stringResource(R.string.menu_button3),
                    Icons.Default.LocalGasStation,
                    onNavigateToFuel
                ),
                MenuItem(
                    stringResource(R.string.menu_button4),
                    Icons.Default.Description,
                    onNavigateToDocs
                ),
            )
            MenuRow(
                modifier = Modifier.weight(1f),
                MenuItem(
                    stringResource(R.string.menu_button5),
                    Icons.Default.Settings, {}
                ),
                MenuItem(
                    stringResource(R.string.menu_button6),
                    Icons.Default.Info, {}
                ),
            )
        }
    }
}

private data class MenuItem(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@Composable
private fun MenuRow(
    modifier: Modifier = Modifier,
    vararg items: MenuItem,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            MenuButton(
                item = item,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            )
        }
    }
}

@Composable
private fun MenuButton(item: MenuItem, modifier: Modifier = Modifier) {
    Card(
        onClick = item.onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MainMenuScreenPreview() {
    PreviewMechanicTheme {
        MainMenuScreen(
            onNavigateToCars = {},
            onNavigateToDrivers = {},
            onNavigateToFuel = {},
            onNavigateToDocs = {},
        )
    }
}
