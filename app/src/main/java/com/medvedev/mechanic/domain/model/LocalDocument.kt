package com.medvedev.mechanic.domain.model

data class LocalDocument(
    val id: String,
    val path: String,
    val isOfflineCopy: Boolean
)
