package com.medvedev.mechanic.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun ListPaneScaffold(
    title: String,
    onBack: () -> Unit,
    showAddButton: Boolean = true,
    onAddClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    val overlayEndPadding = LocalDetailOverlayEndPadding.current

    Scaffold(
        modifier = if (overlayEndPadding > 0.dp) {
            Modifier.consumeWindowInsets(
                WindowInsets.safeDrawing.only(WindowInsetsSides.End),
            )
        } else {
            Modifier
        },
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            MechanicTopBar(title = title, onBack = onBack)
        },
        floatingActionButton = {
            if (showAddButton) {
                FloatingActionButton(
                    modifier = Modifier.padding(end = overlayEndPadding),
                    onClick = onAddClick,
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add),
                    )
                }
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp),
            content = content,
        )
    }
}

@Composable
fun ListSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .padding(end = LocalDetailOverlayEndPadding.current)
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
        ),
    )
}

@Composable
fun <T> ListContent(
    items: List<T>,
    isLoading: Boolean,
    key: (T) -> Any,
    footer: String? = null,
    itemContent: @Composable (T) -> Unit,
) {
    val overlayEndPadding = LocalDetailOverlayEndPadding.current

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = overlayEndPadding),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(end = overlayEndPadding),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = items,
                key = key,
            ) { item ->
                itemContent(item)
            }
            if (!footer.isNullOrEmpty()) {
                item {
                    Text(
                        text = footer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListSearchFieldPreview() {
    PreviewMechanicTheme {
        ListSearchField(
            query = "",
            onQueryChange = {},
            placeholder = "Поиск а/м",
        )
    }
}
