package com.medvedev.mechanic.presentation.docs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.document.DocumentAccess
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.LocalDocument
import com.medvedev.mechanic.domain.result.Result
import com.medvedev.mechanic.domain.usecase.document.GetDocumentUseCase
import com.medvedev.mechanic.domain.usecase.document.PrepareDocumentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PdfDocumentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val prepareDocumentUseCase: PrepareDocumentUseCase,
    private val getDocumentUseCase: GetDocumentUseCase,
) : ViewModel() {

    private val documentId: String = savedStateHandle.get<String>("documentId").orEmpty()

    private var downloadConfirmed = false

    private val _uiState = MutableStateFlow(PdfDocumentUiState(documentId = documentId))
    val uiState: StateFlow<PdfDocumentUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        if (documentId.isBlank()) {
            showError(DomainError.NotFound)
            return
        }

        if (downloadConfirmed) download() else prepare()
    }

    fun confirmDownload() {
        downloadConfirmed = true
        download()
    }

    fun skipDownload() {
        val cached = _uiState.value.downloadRequired?.cached ?: return
        downloadConfirmed = false
        showDocument(cached)
    }

    private fun prepare() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, downloadRequired = null) }
            when (val result = prepareDocumentUseCase(documentId)) {
                is Result.Success -> when (val access = result.data) {
                    is DocumentAccess.Ready -> showDocument(access.document)
                    is DocumentAccess.DownloadRequired -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                document = null,
                                downloadRequired = access,
                                error = null,
                            )
                        }
                    }
                }

                is Result.Error -> showError(result.error)
            }
        }
    }

    private fun download() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, downloadRequired = null) }
            when (val result = getDocumentUseCase(documentId)) {
                is Result.Success -> showDocument(result.data)
                is Result.Error -> showError(result.error)
            }
        }
    }

    private fun showDocument(document: LocalDocument) {
        _uiState.update {
            it.copy(
                isLoading = false,
                document = document,
                downloadRequired = null,
                error = null,
            )
        }
    }

    private fun showError(error: DomainError) {
        _uiState.update {
            it.copy(
                isLoading = false,
                document = null,
                downloadRequired = null,
                error = error,
            )
        }
    }
}
