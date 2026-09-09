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
        _uiState.update { PdfSearchUiState(query = it.query, visible = it.visible) }
        loadJob = viewModelScope.launch {
            val result = loadPdfSearchIndex(path)
            if (indexedPath != path) return@launch
            _uiState.update {
                when (result) {
                    is Result.Success -> it.copy(index = result.data, error = null)
                    is Result.Error -> it.copy(error = result.error, index = null)
                }
            }
        }
    }

    fun setQuery(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun setSearchVisible(visible: Boolean) {
        _uiState.update { it.copy(visible = visible) }
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
                query = "",
                visible = false,
            )
        }
    }
}
