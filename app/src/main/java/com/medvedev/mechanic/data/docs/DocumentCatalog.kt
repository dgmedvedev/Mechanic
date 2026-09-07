package com.medvedev.mechanic.data.docs

import androidx.annotation.StringRes
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.document.DocumentIds

object DocumentCatalog {

    private const val URL_RESOLUTION_44 =
        "https://transtekhnika.by/uslugi/razrabotka-norm-rashoda-topliva/" +
                "%D0%9F%D0%BE%D1%81%D1%82%D0%B0%D0%BD%D0%BE%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5%20" +
                "%D0%9C%D0%B8%D0%BD%D1%82%D1%80%D0%B0%D0%BD%D1%81%D0%B0%20N%2044%20%D0%BE%D1%82%20" +
                "01.08.2019.pdf"

    private const val URL_RESOLUTION_470 =
        "https://minfin.gov.by/upload/kru/acts/postsm/postsm_100820_470.pdf"

    val entries = listOf(
        DocumentCatalogEntry(
            id = DocumentIds.RESOLUTION_44,
            url = URL_RESOLUTION_44,
            titleRes = R.string.resolution_44,
        ),
        DocumentCatalogEntry(
            id = DocumentIds.RESOLUTION_470,
            url = URL_RESOLUTION_470,
            titleRes = R.string.resolution_470,
        ),
    )

    fun urlFor(id: String): String? = entries.find { it.id == id }?.url
}

data class DocumentCatalogEntry(
    val id: String,
    val url: String,
    @param:StringRes val titleRes: Int,
)
