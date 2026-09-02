package com.medvedev.mechanic.presentation.docs.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.model.PdfSearchMatch
import com.medvedev.mechanic.domain.result.Result
import com.medvedev.mechanic.domain.usecase.document.LoadPdfSearchIndexUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PdfSearchViewModel @Inject constructor(
    private val loadPdfSearchIndex: LoadPdfSearchIndexUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PdfSearchUiState())
    val uiState: StateFlow<PdfSearchUiState> = _uiState.asStateFlow()

    private var indexedPath: String? = null
    private var loadJob: Job? = null

    fun loadIfNeeded(path: String) {
        val state = _uiState.value
        if (indexedPath == path &&
            (state.index != null || state.error != null || loadJob?.isActive == true)
        ) {
            return
        }
        loadJob?.cancel()
        indexedPath = path
        _uiState.value = PdfSearchUiState()
        loadJob = viewModelScope.launch {
            val update = when (val result = loadPdfSearchIndex(path)) {
                is Result.Success -> PdfSearchUiState(index = result.data)
                is Result.Error -> PdfSearchUiState(error = result.error)
            }
            if (indexedPath == path) {
                _uiState.value = update
            }
        }
    }

    fun applySearch(query: String, results: List<PdfSearchMatch>) {
        _uiState.update {
            it.copy(
                committedQuery = query,
                matches = results,
                matchIndex = 0,
            )
        }
    }

    fun selectMatch(index: Int) {
        _uiState.update { state ->
            val size = state.matches.size
            if (size == 0) {
                state
            } else {
                state.copy(matchIndex = (index % size + size) % size)
            }
        }
    }

    fun clearSearch() {
        _uiState.update {
            it.copy(
                committedQuery = null,
                matches = emptyList(),
                matchIndex = 0,
            )
        }
    }
}
