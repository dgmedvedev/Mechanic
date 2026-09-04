package com.medvedev.mechanic.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    onValueChange: ((String) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val valueStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Start,
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        val valueModifier = Modifier
            .weight(2f)
            .padding(start = 12.dp)

        when {
            onClick != null -> {
                Text(
                    text = value.ifBlank { "—" },
                    modifier = valueModifier
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(role = Role.Button, onClick = onClick)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    style = valueStyle,
                )
            }

            onValueChange == null -> {
                Text(
                    text = value.ifBlank { "—" },
                    modifier = valueModifier
                        .border(1.dp, Color.Transparent, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    style = valueStyle,
                )
            }

            else -> {
                val interactionSource = remember { MutableInteractionSource() }
                val isFocused by interactionSource.collectIsFocusedAsState()
                val borderColor = if (isFocused) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = valueModifier
                        .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    singleLine = true,
                    textStyle = valueStyle,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    interactionSource = interactionSource,
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.isEmpty()) {
                                Text(text = "—", style = valueStyle)
                            }
                            innerTextField()
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun DetailContentLayout(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    showDelete: Boolean = true,
    editing: Boolean = false,
    showClose: Boolean = true,
    onCloseClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    saveEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            content = content,
        )
        DetailActionBar(
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            showDelete = showDelete,
            editing = editing,
            showClose = showClose,
            onCloseClick = onCloseClick,
            onSaveClick = onSaveClick,
            saveEnabled = saveEnabled,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
        )
    }
}

@Composable
fun DetailActionBar(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    showDelete: Boolean = true,
    editing: Boolean = false,
    showClose: Boolean = true,
    onCloseClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    saveEnabled: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (editing) {
            if (showClose) {
                DetailActionIconButton(
                    onClick = onCloseClick,
                    icon = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.close),
                )
            }
            DetailActionIconButton(
                onClick = onSaveClick,
                icon = Icons.Outlined.Save,
                contentDescription = stringResource(R.string.save),
                enabled = saveEnabled,
            )
        } else {
            DetailActionIconButton(
                onClick = onEditClick,
                icon = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.edit),
            )
            if (showDelete) {
                DetailActionIconButton(
                    onClick = onDeleteClick,
                    icon = Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.delete),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
private fun DetailActionIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.primary,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (enabled) tint else tint.copy(alpha = 0.38f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailRowPreview() {
    PreviewMechanicTheme {
        DetailContentLayout {
            DetailRow(label = "Марка", value = "Audi", icon = Icons.Outlined.DirectionsCar)
            DetailRow(label = "Год выпуска", value = "2010", icon = Icons.Outlined.CalendarMonth)
            DetailRow(label = "Госномер", value = "", icon = Icons.Outlined.DirectionsCar)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailRowEditingPreview() {
    PreviewMechanicTheme {
        DetailContentLayout(
            editing = true,
        ) {
            DetailRow(
                label = "Марка",
                value = "Audi",
                icon = Icons.Outlined.DirectionsCar,
                onValueChange = {},
            )
            DetailRow(
                label = "Год выпуска",
                value = "2010",
                icon = Icons.Outlined.CalendarMonth,
                onValueChange = {},
            )
            DetailRow(
                label = "Госномер",
                value = "",
                icon = Icons.Outlined.DirectionsCar,
                onValueChange = {},
            )
        }
    }
}
