package com.medvedev.mechanic.presentation.docs.viewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.PdfSearchMatch
import com.medvedev.mechanic.presentation.docs.DocumentMessage
import com.medvedev.mechanic.presentation.error.toMessageRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.roundToInt

internal const val MIN_ZOOM = 1f
internal const val MAX_ZOOM = 3f
private const val ZOOM_STEP = 0.25f
private const val MAX_RENDER_ZOOM = 2f
private const val RENDER_DEBOUNCE_MS = 150L
private const val SEARCH_DEBOUNCE_MS = 250L
private val SearchBarScrollMargin = 72.dp

@Composable
internal fun PdfPages(
    path: String,
    modifier: Modifier = Modifier,
) {
    val renderer = rememberPdfRendererSession(path)
    var hasRenderedPage by remember(path) { mutableStateOf(false) }

    Box(modifier) {
        val currentSession = renderer.session
        when {
            renderer.failed -> {
                DocumentMessage(
                    message = stringResource(DomainError.Network.InvalidContent.toMessageRes()),
                    onAction = null,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            currentSession != null -> {
                PdfPageList(
                    path = path,
                    session = currentSession,
                    hasRenderedPage = hasRenderedPage,
                    onRendered = { hasRenderedPage = true },
                )
            }
        }

        if (!renderer.failed && !hasRenderedPage) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun PdfPageList(
    path: String,
    session: PdfRendererSession,
    hasRenderedPage: Boolean,
    onRendered: () -> Unit,
    searchViewModel: PdfSearchViewModel = hiltViewModel(),
) {
    val density = LocalDensity.current
    val listState = rememberLazyListState()
    val horizontalScroll = rememberScrollState()

    val searchState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val searchIndex = searchState.index
    val matches = searchState.matches
    val matchIndex = searchState.matchIndex

    var zoom by rememberSaveable { mutableFloatStateOf(MIN_ZOOM) }
    var toolbarVisible by rememberSaveable { mutableStateOf(true) }
    var searchVisible by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var focusSearch by remember { mutableStateOf(false) }
    var committedRenderWidth by remember { mutableIntStateOf(0) }
    var viewportWidthPx by remember { mutableIntStateOf(1) }
    val keyboard = LocalSoftwareKeyboardController.current

    fun applyZoom(newZoom: Float) {
        zoom = newZoom.coerceIn(MIN_ZOOM, MAX_ZOOM)
    }

    suspend fun scrollToMatch(match: PdfSearchMatch, widthPx: Int) {
        val displayWidthPx = (widthPx * zoom - with(density) { 16.dp.toPx() })
            .coerceAtLeast(1f)
        val pageHeightPx = displayWidthPx * (match.pageHeight / match.pageWidth.coerceAtLeast(1f))
        val matchTop = match.rects.minOfOrNull { it.top } ?: 0f
        val matchLeft = match.rects.minOfOrNull { it.left } ?: 0f
        val marginPx = with(density) { SearchBarScrollMargin.toPx() }
        val offset = (matchTop * pageHeightPx - marginPx).roundToInt().coerceAtLeast(0)
        listState.animateScrollToItem(match.pageIndex, offset)
        if (zoom > MIN_ZOOM + 0.01f) {
            horizontalScroll.animateScrollTo(
                (matchLeft * displayWidthPx - marginPx).roundToInt().coerceAtLeast(0),
            )
        }
    }

    fun goToMatch(index: Int) {
        if (matches.isEmpty()) return
        keyboard?.hide()
        searchViewModel.selectMatch(index)
    }

    LaunchedEffect(hasRenderedPage, path) {
        if (!hasRenderedPage) return@LaunchedEffect
        searchViewModel.loadIfNeeded(path)
    }

    LaunchedEffect(searchQuery, searchIndex, searchVisible) {
        if (!searchVisible || searchIndex == null) return@LaunchedEffect
        if (searchQuery == searchState.committedQuery) return@LaunchedEffect
        delay(SEARCH_DEBOUNCE_MS)
        val result = withContext(Dispatchers.Default) {
            searchIndex.search(searchQuery)
        }
        searchViewModel.applySearch(searchQuery, result)
    }

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        val measuredViewportWidthPx = constraints.maxWidth.coerceAtLeast(1)
        SideEffect { viewportWidthPx = measuredViewportWidthPx }
        val viewportHeight = maxHeight
        val zoomedWidth = with(density) { (measuredViewportWidthPx * zoom).toDp() }

        LaunchedEffect(zoom, measuredViewportWidthPx) {
            val target = (measuredViewportWidthPx * zoom.coerceAtMost(MAX_RENDER_ZOOM))
                .roundToInt()
                .coerceAtLeast(1)
            if (committedRenderWidth == 0) {
                committedRenderWidth = target
                return@LaunchedEffect
            }
            delay(RENDER_DEBOUNCE_MS)
            committedRenderWidth = target
        }

        val renderWidthPx = committedRenderWidth.takeIf { it > 0 } ?: viewportWidthPx

        LaunchedEffect(
            searchVisible,
            hasRenderedPage,
            measuredViewportWidthPx,
            zoom,
            matchIndex,
            matches,
        ) {
            if (!searchVisible || !hasRenderedPage) return@LaunchedEffect
            val match = matches.getOrNull(matchIndex) ?: return@LaunchedEffect
            scrollToMatch(match, measuredViewportWidthPx)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pdfViewerGestures(
                    zoomProvider = { zoom },
                    onZoom = { applyZoom(it) },
                    onSwipeToEnd = { toolbarVisible = false },
                    onSwipeToStart = { toolbarVisible = true },
                )
                .horizontalScroll(horizontalScroll, enabled = zoom > MIN_ZOOM + 0.01f),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .width(zoomedWidth)
                    .height(viewportHeight),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(session.pageCount) { index ->
                    PdfPage(
                        session = session,
                        pageIndex = index,
                        widthPx = renderWidthPx,
                        displayWidth = zoomedWidth - 16.dp,
                        highlights = matches.mapIndexedNotNull { matchI, match ->
                            if (match.pageIndex != index) {
                                null
                            } else {
                                PdfPageHighlight(
                                    rects = match.rects,
                                    isCurrent = matchI == matchIndex,
                                )
                            }
                        },
                        onRendered = onRendered,
                    )
                }
            }
        }

        if (searchVisible) {
            PdfSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                loading = searchIndex == null && searchState.error == null,
                error = searchState.error,
                requestFocus = focusSearch,
                onFocusRequested = { focusSearch = false },
                matchIndex = matchIndex,
                matchCount = matches.size,
                onPrevious = { goToMatch(matchIndex - 1) },
                onNext = { goToMatch(matchIndex + 1) },
                onClose = {
                    keyboard?.hide()
                    searchVisible = false
                    searchQuery = ""
                    searchViewModel.clearSearch()
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(2f)
                    .padding(8.dp),
            )
        }

        AnimatedVisibility(
            visible = hasRenderedPage && toolbarVisible,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .zIndex(1f)
                .padding(end = 16.dp),
            enter = slideInHorizontally { it } + fadeIn(),
            exit = slideOutHorizontally { it } + fadeOut(),
        ) {
            DocumentFloatingToolbar(
                zoom = zoom,
                onSearch = {
                    if (searchVisible) {
                        searchVisible = false
                    } else {
                        searchVisible = true
                        focusSearch = true
                    }
                },
                onZoomIn = { applyZoom(zoom + ZOOM_STEP) },
                onZoomOut = { applyZoom(zoom - ZOOM_STEP) },
            )
        }
    }
}

@Composable
private fun Modifier.pdfViewerGestures(
    zoomProvider: () -> Float,
    onZoom: (Float) -> Unit,
    onSwipeToEnd: () -> Unit,
    onSwipeToStart: () -> Unit,
): Modifier {
    val currentZoom = rememberUpdatedState(zoomProvider)
    val currentOnZoom = rememberUpdatedState(onZoom)
    val currentOnSwipeToEnd = rememberUpdatedState(onSwipeToEnd)
    val currentOnSwipeToStart = rememberUpdatedState(onSwipeToStart)

    return pointerInput(Unit) {
        val touchSlop = viewConfiguration.touchSlop
        val swipeThreshold = 64.dp.toPx()

        awaitEachGesture {
            awaitFirstDown(requireUnconsumed = false)
            var zoomSoFar = 1f
            var panX = 0f
            var panY = 0f
            var pinchActive = false
            var horizontalSwipe = false
            var gestureDecided = false
            val swipeEnabled = currentZoom.value() <= MIN_ZOOM + 0.01f

            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                val pressed = event.changes.filter { it.pressed }
                if (pressed.isEmpty()) break

                if (pressed.size >= 2) {
                    val zoomChange = event.calculateZoom()
                    zoomSoFar *= zoomChange
                    if (!pinchActive) {
                        val motion = abs(1f - zoomSoFar) * event.calculateCentroidSize()
                        if (motion > touchSlop) pinchActive = true
                    }
                    if (pinchActive && zoomChange != 1f) {
                        currentOnZoom.value(currentZoom.value() * zoomChange)
                        event.changes.forEach { it.consume() }
                    }
                } else if (!pinchActive && swipeEnabled) {
                    val change = pressed.first()
                    panX += change.position.x - change.previousPosition.x
                    panY += change.position.y - change.previousPosition.y
                    if (!gestureDecided && (abs(panX) > touchSlop || abs(panY) > touchSlop)) {
                        gestureDecided = true
                        horizontalSwipe = abs(panX) > abs(panY)
                    }
                    if (horizontalSwipe) {
                        change.consume()
                    }
                }
            }

            if (!pinchActive && horizontalSwipe && abs(panX) > swipeThreshold) {
                if (panX > 0f) currentOnSwipeToEnd.value() else currentOnSwipeToStart.value()
            }
        }
    }
}
