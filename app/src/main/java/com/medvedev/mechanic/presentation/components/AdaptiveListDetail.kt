package com.medvedev.mechanic.presentation.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

val ExpandedListDetailBreakpoint = 600.dp

val LocalDetailOverlayEndPadding = compositionLocalOf { 0.dp }

private const val DetailOverlayWidthFraction = 0.55f

@Composable
fun AdaptiveListDetail(
    isExpanded: Boolean,
    listContent: @Composable () -> Unit,
    detailContent: @Composable () -> Unit,
    showDetail: Boolean,
) {
    if (!isExpanded) {
        if (showDetail) detailContent() else listContent()
        return
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val overlayEndPadding = if (showDetail) maxWidth * DetailOverlayWidthFraction else 0.dp

        CompositionLocalProvider(LocalDetailOverlayEndPadding provides overlayEndPadding) {
            listContent()
        }

        if (showDetail) {
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .fillMaxWidth(DetailOverlayWidthFraction),
                shape = RoundedCornerShape(16.dp),
            ) {
                detailContent()
            }
        }
    }
}
