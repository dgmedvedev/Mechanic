package com.medvedev.mechanic.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

data class TopLevelNav(
    val currentRoute: String?,
    val onTabClick: (String) -> Unit,
    val wideLayout: Boolean,
)

val LocalTopLevelNav = compositionLocalOf<TopLevelNav?> { null }

private data class TopLevelTab(
    val route: String,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val TopLevelTabs = listOf(
    TopLevelTab(
        Routes.CARS,
        R.string.cars,
        Icons.Filled.DirectionsCar,
        Icons.Outlined.DirectionsCar,
    ),
    TopLevelTab(
        Routes.DRIVERS,
        R.string.drivers,
        Icons.Filled.People,
        Icons.Outlined.People,
    ),
    TopLevelTab(
        Routes.DOCS,
        R.string.documents,
        Icons.Filled.Description,
        Icons.Outlined.Description,
    ),
)

private val NavBarItemHeight = 48.dp
private val NavIconSize = 24.dp
private val NavLabelSize = 11.sp
private val NavHairline = 0.5.dp

@Composable
fun MechanicTopLevelNavigationBar(
    modifier: Modifier = Modifier,
    endPadding: Dp = 0.dp,
) {
    val nav = LocalTopLevelNav.current ?: return
    MechanicNavigationBar(
        currentRoute = nav.currentRoute,
        onTabClick = nav.onTabClick,
        iconsOnly = nav.wideLayout,
        omitEndWindowInsets = endPadding > 0.dp,
        modifier = modifier.padding(end = endPadding),
    )
}

@Composable
fun MechanicNavigationBar(
    currentRoute: String?,
    onTabClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    iconsOnly: Boolean = false,
    omitEndWindowInsets: Boolean = false,
) {
    val horizontalInsets = if (omitEndWindowInsets) {
        WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
    } else {
        WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
    }
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(Modifier.fillMaxWidth()) {
            HorizontalDivider(
                thickness = NavHairline,
                color = MaterialTheme.colorScheme.outline,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(horizontalInsets)
                    .height(NavBarItemHeight)
                    .padding(top = if (iconsOnly) 0.dp else 4.dp)
                    .selectableGroup(),
                verticalAlignment = if (iconsOnly) {
                    Alignment.CenterVertically
                } else {
                    Alignment.Top
                },
            ) {
                TopLevelTabs.forEach { tab ->
                    val selected = currentRoute == tab.route
                    MechanicNavDestination(
                        selected = selected,
                        onClick = { onTabClick(tab.route) },
                        icon = if (selected) tab.selectedIcon else tab.unselectedIcon,
                        label = stringResource(tab.labelRes),
                        iconsOnly = iconsOnly,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}

@Composable
private fun MechanicNavDestination(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    iconsOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Column(
        modifier = modifier.selectable(
            selected = selected,
            onClick = onClick,
            role = Role.Tab,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(NavIconSize),
            tint = color,
        )
        if (!iconsOnly) {
            Text(
                text = label,
                color = color,
                fontSize = NavLabelSize,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun MechanicNavigationBarPreview() {
    PreviewMechanicTheme {
        MechanicNavigationBar(
            currentRoute = Routes.CARS,
            onTabClick = {},
        )
    }
}

@PreviewLightDark
@Composable
private fun MechanicNavigationBarIconsOnlyPreview() {
    PreviewMechanicTheme {
        MechanicNavigationBar(
            currentRoute = Routes.CARS,
            onTabClick = {},
            iconsOnly = true,
        )
    }
}
