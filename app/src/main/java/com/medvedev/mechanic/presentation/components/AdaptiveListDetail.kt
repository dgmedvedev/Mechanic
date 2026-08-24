package com.medvedev.mechanic.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

val ExpandedListDetailBreakpoint = 600.dp

val LocalDetailOverlayEndPadding = compositionLocalOf { 0.dp }

val LocalInAdaptiveDetailOverlay = compositionLocalOf { false }

private const val DetailOverlayWidthFraction = 0.55f

fun resolveDetailId(selectedId: String?, visibleIds: List<String>): String? =
    selectedId?.takeIf { it in visibleIds } ?: visibleIds.firstOrNull()

@Composable
fun AdaptiveListDetail(
    isExpanded: Boolean,
    listContent: @Composable () -> Unit,
    detailContent: @Composable () -> Unit,
) {
    if (!isExpanded) {
        listContent()
        return
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val overlayEndPadding = maxWidth * DetailOverlayWidthFraction

        CompositionLocalProvider(LocalDetailOverlayEndPadding provides overlayEndPadding) {
            listContent()
        }

        Surface(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .fillMaxWidth(DetailOverlayWidthFraction),
            shape = RoundedCornerShape(16.dp),
        ) {
            CompositionLocalProvider(LocalInAdaptiveDetailOverlay provides true) {
                detailContent()
            }
        }
    }
}

@Composable
fun AdaptiveDetailPane(
    isLoading: Boolean,
    detailId: String?,
    emptyMessage: String,
    content: @Composable (String) -> Unit,
) {
    when {
        detailId != null -> content(detailId)
        isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        else -> DetailEmptyPlaceholder(emptyMessage)
    }
}

@Composable
private fun DetailEmptyPlaceholder(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 600)
@Composable
private fun AdaptiveListDetailPreview() {
    PreviewMechanicTheme {
        AdaptiveListDetail(
            isExpanded = true,
            listContent = {
                Text(
                    text = "ListContent",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            detailContent = {
                Text(
                    text = "DetailContent",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
        )
    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 600)
@Composable
private fun AdaptiveListDetailEmptyPreview() {
    PreviewMechanicTheme {
        AdaptiveListDetail(
            isExpanded = true,
            listContent = {
                Text(
                    text = "ListContent",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            detailContent = {
                DetailEmptyPlaceholder("Добавьте автомобиль, чтобы увидеть его данные")
            },
        )
    }
}
