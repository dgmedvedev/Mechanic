package com.medvedev.mechanic.presentation.docs.viewer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.presentation.error.toMessageRes
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
internal fun DocumentFloatingToolbar(
    zoom: Float,
    onSearch: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(percent = 50),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 4.dp,
        tonalElevation = 3.dp,
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.document_search),
                )
            }
            IconButton(
                onClick = onZoomIn,
                enabled = zoom < MAX_ZOOM,
            ) {
                Icon(
                    imageVector = Icons.Filled.ZoomIn,
                    contentDescription = stringResource(R.string.document_zoom_in),
                )
            }
            IconButton(
                onClick = onZoomOut,
                enabled = zoom > MIN_ZOOM,
            ) {
                Icon(
                    imageVector = Icons.Filled.ZoomOut,
                    contentDescription = stringResource(R.string.document_zoom_out),
                )
            }
        }
    }
}

@Composable
internal fun PdfSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    loading: Boolean,
    error: DomainError?,
    requestFocus: Boolean,
    onFocusRequested: () -> Unit,
    matchIndex: Int,
    matchCount: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(requestFocus) {
        if (!requestFocus) return@LaunchedEffect
        focusRequester.requestFocus()
        onFocusRequested()
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        tonalElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .focusRequester(focusRequester),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { keyboard?.hide() },
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                text = stringResource(R.string.document_search_placeholder),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        innerTextField()
                    }
                },
            )
            when {
                loading -> {
                    Row(
                        modifier = Modifier.padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                        )
                        Text(
                            text = stringResource(R.string.document_search_preparing),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                        )
                    }
                }

                error != null -> {
                    Text(
                        text = stringResource(
                            if (error == DomainError.Storage.Full) {
                                error.toMessageRes()
                            } else {
                                R.string.document_search_unavailable
                            }
                        ),
                        modifier = Modifier.widthIn(max = 160.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                query.isNotBlank() -> {
                    Text(
                        text = if (matchCount == 0) {
                            stringResource(R.string.document_search_no_matches)
                        } else {
                            stringResource(
                                R.string.document_search_matches,
                                matchIndex + 1,
                                matchCount,
                            )
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            IconButton(
                onClick = onPrevious,
                enabled = matchCount > 0,
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = stringResource(R.string.document_search_previous),
                )
            }
            IconButton(
                onClick = onNext,
                enabled = matchCount > 0,
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.document_search_next),
                )
            }
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.document_search_close),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun DocumentFloatingToolbarPreview() {
    PreviewMechanicTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DocumentFloatingToolbar(
                zoom = MIN_ZOOM,
                onSearch = {},
                onZoomIn = {},
                onZoomOut = {},
            )
            DocumentFloatingToolbar(
                zoom = MAX_ZOOM,
                onSearch = {},
                onZoomIn = {},
                onZoomOut = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PdfSearchBarPreview() {
    PreviewMechanicTheme {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PdfSearchBarPreviewItem(query = "")
            PdfSearchBarPreviewItem(query = "Audi", loading = true)
            PdfSearchBarPreviewItem(query = "Ауди")
            PdfSearchBarPreviewItem(query = "TT", matchIndex = 0, matchCount = 4)
            PdfSearchBarPreviewItem(
                query = "Audi",
                error = DomainError.Unknown,
            )
        }
    }
}

@Composable
private fun PdfSearchBarPreviewItem(
    query: String,
    loading: Boolean = false,
    error: DomainError? = null,
    matchIndex: Int = 0,
    matchCount: Int = 0,
) {
    PdfSearchBar(
        query = query,
        onQueryChange = {},
        loading = loading,
        error = error,
        requestFocus = false,
        onFocusRequested = {},
        matchIndex = matchIndex,
        matchCount = matchCount,
        onPrevious = {},
        onNext = {},
        onClose = {},
    )
}
