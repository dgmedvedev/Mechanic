package com.medvedev.mechanic.presentation.docs

import androidx.annotation.StringRes
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.document.DocumentIds

data class NormativeDocItem(
    val id: String,
    @param:StringRes val titleRes: Int,
)

object NormativeDocsCatalog {
    val items = listOf(
        NormativeDocItem(DocumentIds.FUEL_NORMS, R.string.norms),
        NormativeDocItem(DocumentIds.RESOLUTION_470, R.string.resolution470),
    )

    @StringRes
    fun titleResFor(id: String): Int =
        items.find { it.id == id }?.titleRes ?: R.string.normative_documents
}
