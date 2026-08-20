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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R

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
        topBar = {
            MechanicTopBar(title = title, onBack = onBack)
        },
        floatingActionButton = {
            if (showAddButton) {
                FloatingActionButton(
                    modifier = Modifier.padding(end = overlayEndPadding),
                    onClick = onAddClick,
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
        singleLine = true,
    )
}

@Composable
fun <T> ListContent(
    items: List<T>,
    isLoading: Boolean,
    key: (T) -> Any,
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
        }
    }
}
