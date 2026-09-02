package com.medvedev.mechanic.presentation.docs.viewer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.medvedev.mechanic.domain.model.PdfNormalizedRect
import com.medvedev.mechanic.presentation.theme.PdfHighlight
import kotlin.coroutines.cancellation.CancellationException

private const val PAGE_PLACEHOLDER_ASPECT = 1f / 1.4142f
private const val HIGHLIGHT_CURRENT_ALPHA = 0.6f
private const val HIGHLIGHT_OTHER_ALPHA = 0.16f

internal data class PdfPageHighlight(
    val rects: List<PdfNormalizedRect>,
    val isCurrent: Boolean,
)

@Composable
internal fun PdfPage(
    session: PdfRendererSession,
    pageIndex: Int,
    widthPx: Int,
    displayWidth: Dp,
    highlights: List<PdfPageHighlight> = emptyList(),
    onRendered: () -> Unit = {},
) {
    var image by remember(session, pageIndex, widthPx) {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(session, pageIndex, widthPx) {
        image = try {
            session.renderPage(pageIndex, widthPx)
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            null
        }
        onRendered()
    }

    val pageImage = image
    val currentHighlight = PdfHighlight.copy(alpha = HIGHLIGHT_CURRENT_ALPHA)
    val otherHighlight = PdfHighlight.copy(alpha = HIGHLIGHT_OTHER_ALPHA)
    Box(modifier = Modifier.width(displayWidth)) {
        if (pageImage != null) {
            Image(
                bitmap = pageImage,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(PAGE_PLACEHOLDER_ASPECT)
                    .background(Color.White),
            )
        }
        if (highlights.isNotEmpty()) {
            Canvas(Modifier.matchParentSize()) {
                highlights.forEach { highlight ->
                    val color = if (highlight.isCurrent) currentHighlight else otherHighlight
                    highlight.rects.forEach { rect ->
                        drawRect(
                            color = color,
                            topLeft = Offset(
                                x = rect.left * size.width,
                                y = rect.top * size.height,
                            ),
                            size = Size(
                                width = rect.width * size.width,
                                height = rect.height * size.height,
                            ),
                        )
                    }
                }
            }
        }
    }
}
