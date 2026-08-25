package com.medvedev.mechanic.presentation.common

interface UiState

data class ListUiState<T>(
    val items: List<T> = emptyList(),
    val filteredItems: List<T> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
) : UiState

data class DetailUiState<T>(
    val item: T? = null,
    val isLoading: Boolean = true,
    val notFound: Boolean = false,
) : UiState
