package com.medvedev.mechanic.presentation.docs.viewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.awaitCancellation
import kotlin.coroutines.cancellation.CancellationException

internal data class PdfRendererSessionState(
    val session: PdfRendererSession? = null,
    val failed: Boolean = false,
)

@Composable
internal fun rememberPdfRendererSession(path: String): PdfRendererSessionState {
    var session by remember(path) { mutableStateOf<PdfRendererSession?>(null) }
    var failed by remember(path) { mutableStateOf(false) }

    LaunchedEffect(path) {
        session = null
        failed = false
        try {
            val opened = PdfRendererSession.open(path)
            session = opened
            try {
                awaitCancellation()
            } finally {
                opened.close()
                session = null
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            failed = true
        }
    }

    return PdfRendererSessionState(session = session, failed = failed)
}
