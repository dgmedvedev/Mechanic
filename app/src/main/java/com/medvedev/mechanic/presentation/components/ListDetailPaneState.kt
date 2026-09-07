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
    isAdding: Boolean = false,
) {
    var selectedId by mutableStateOf(selectedId)
        private set
    var editingId by mutableStateOf(editingId)
        private set
    var isAdding by mutableStateOf(isAdding)
        private set

    fun select(id: String?) {
        selectedId = id
        isAdding = false
        if (id != editingId) editingId = null
    }

    fun startAdding() {
        isAdding = true
        editingId = null
    }

    fun stopAdding() {
        isAdding = false
    }

    fun startEditing(id: String) {
        editingId = id
        isAdding = false
    }

    fun stopEditing() {
        editingId = null
    }

    fun clear() {
        selectedId = null
        editingId = null
        isAdding = false
    }
}

@Composable
fun rememberListDetailPaneState(): ListDetailPaneState =
    rememberSaveable(saver = ListDetailPaneStateSaver) { ListDetailPaneState() }

private val ListDetailPaneStateSaver = listSaver<ListDetailPaneState, String?>(
    save = { listOf(it.selectedId, it.editingId, it.isAdding.toString()) },
    restore = {
        ListDetailPaneState(
            it.getOrNull(0),
            it.getOrNull(1),
            it.getOrNull(2).toBoolean(),
        )
    },
)

fun resolveDetailId(selectedId: String?, visibleIds: List<String>): String? =
    selectedId?.takeIf { it in visibleIds } ?: visibleIds.firstOrNull()
