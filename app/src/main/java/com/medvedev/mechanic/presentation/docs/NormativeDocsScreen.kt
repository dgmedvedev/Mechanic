package com.medvedev.mechanic.presentation.docs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.document.DocumentIds
import com.medvedev.mechanic.domain.model.NormativeDocument
import com.medvedev.mechanic.presentation.components.AdaptiveDetailPane
import com.medvedev.mechanic.presentation.components.AdaptiveListDetail
import com.medvedev.mechanic.presentation.components.ExpandedListDetailBreakpoint
import com.medvedev.mechanic.presentation.components.ListContent
import com.medvedev.mechanic.presentation.components.ListPaneScaffold
import com.medvedev.mechanic.presentation.components.ListSearchField
import com.medvedev.mechanic.presentation.components.rememberListDetailPaneState
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun NormativeDocsScreen(
    onNavigateToDocument: (String) -> Unit,
    documentContent: @Composable (documentId: String, onClose: () -> Unit) -> Unit = { _, _ -> },
    viewModel: DocsListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val paneState = rememberListDetailPaneState()
    val visibleIds = uiState.filteredItems.map { it.id }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= ExpandedListDetailBreakpoint
        val detailId = paneState.selectedId?.takeIf { isExpanded && it in visibleIds }
        val detailEmptyMessage =
            if (uiState.filteredItems.isEmpty() && uiState.searchQuery.isNotBlank()) {
                stringResource(R.string.detail_empty_search)
            } else {
                stringResource(R.string.detail_empty_document)
            }

        AdaptiveListDetail(
            isExpanded = isExpanded,
            listContent = {
                DocsListPane(
                    documents = uiState.filteredItems,
                    isLoading = uiState.isLoading,
                    searchQuery = uiState.searchQuery,
                    selectedDocumentId = detailId,
                    onSearchChange = viewModel::onSearchQueryChange,
                    onDocumentClick = { documentId ->
                        if (isExpanded) {
                            paneState.select(documentId)
                        } else {
                            onNavigateToDocument(documentId)
                        }
                    },
                )
            },
            detailContent = {
                AdaptiveDetailPane(
                    isLoading = uiState.isLoading,
                    detailId = detailId,
                    emptyMessage = detailEmptyMessage,
                    content = { documentId ->
                        documentContent(documentId, paneState::clear)
                    },
                )
            },
        )
    }
}

@Composable
private fun DocsListPane(
    documents: List<NormativeDocument>,
    isLoading: Boolean,
    searchQuery: String,
    selectedDocumentId: String?,
    onSearchChange: (String) -> Unit,
    onDocumentClick: (String) -> Unit,
) {
    ListPaneScaffold(
        title = stringResource(R.string.normative_documents),
        showAddButton = false,
    ) {
        ListSearchField(
            query = searchQuery,
            onQueryChange = onSearchChange,
            placeholder = stringResource(R.string.search_document),
        )
        ListContent(
            items = documents,
            isLoading = isLoading,
            key = { it.id },
        ) { item ->
            DocMenuItem(
                title = item.title,
                selected = item.id == selectedDocumentId,
                onClick = { onDocumentClick(item.id) },
            )
        }
    }
}

@Composable
private fun DocMenuItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 0.dp else 1.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            },
        ),
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
private fun DocsListPanePreview() {
    PreviewMechanicTheme {
        DocsListPane(
            documents = listOf(
                NormativeDocument(DocumentIds.RESOLUTION_44, "Постановление №44"),
                NormativeDocument(DocumentIds.RESOLUTION_470, "Постановление №470"),
            ),
            isLoading = false,
            searchQuery = "",
            selectedDocumentId = DocumentIds.RESOLUTION_44,
            onSearchChange = {},
            onDocumentClick = {},
        )
    }
}
