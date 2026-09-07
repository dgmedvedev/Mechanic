package com.medvedev.mechanic.presentation.docs

import androidx.lifecycle.ViewModel
import com.medvedev.mechanic.domain.model.NormativeDocument
import com.medvedev.mechanic.domain.usecase.document.GetNormativeDocumentsUseCase
import com.medvedev.mechanic.presentation.common.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DocsListViewModel @Inject constructor(
    getNormativeDocumentsUseCase: GetNormativeDocumentsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState<NormativeDocument>(isLoading = false))
    val uiState: StateFlow<ListUiState<NormativeDocument>> = _uiState.asStateFlow()

    init {
        val documents = getNormativeDocumentsUseCase()
        _uiState.value = ListUiState(
            items = documents,
            filteredItems = documents,
            isLoading = false,
        )
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            state.copy(
                filteredItems = filter(state.items, query),
                searchQuery = query,
            )
        }
    }

    private fun filter(list: List<NormativeDocument>, query: String): List<NormativeDocument> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return list

        return list.filter { document ->
            document.title.contains(normalizedQuery, ignoreCase = true)
        }
    }
}
