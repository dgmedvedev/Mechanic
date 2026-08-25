package com.medvedev.mechanic.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class ListDetailPaneState(
    selectedId: String? = null,
    editingId: String? = null,
) {
    var selectedId by mutableStateOf(selectedId)
        private set
    var editingId by mutableStateOf(editingId)
        private set

    fun select(id: String?) {
        selectedId = id
        if (id != editingId) editingId = null
    }

    fun startEditing(id: String) {
        editingId = id
    }

    fun stopEditing() {
        editingId = null
    }

    fun clear() {
        selectedId = null
        editingId = null
    }
}

@Composable
fun rememberListDetailPaneState(): ListDetailPaneState =
    rememberSaveable(saver = ListDetailPaneStateSaver) { ListDetailPaneState() }

private val ListDetailPaneStateSaver = listSaver(
    save = { listOf(it.selectedId, it.editingId) },
    restore = { ListDetailPaneState(it.getOrNull(0), it.getOrNull(1)) },
)

fun resolveDetailId(selectedId: String?, visibleIds: List<String>): String? =
    selectedId?.takeIf { it in visibleIds } ?: visibleIds.firstOrNull()
