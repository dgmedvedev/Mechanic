package com.medvedev.mechanic.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveListDetail(
    listContent: @Composable () -> Unit,
    detailContent: @Composable () -> Unit,
    showDetail: Boolean,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpanded = maxWidth >= 600.dp

        if (isExpanded) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                ) {
                    listContent()
                }
                if (showDetail) {
                    VerticalDivider()
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .fillMaxWidth(),
                    ) {
                        Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                            ) {
                                detailContent()
                            }
                        }
                    }
                }
            }
        } else if (showDetail) {
            detailContent()
        } else {
            listContent()
        }
    }
}
