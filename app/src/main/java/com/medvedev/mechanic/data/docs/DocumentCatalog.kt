package com.medvedev.mechanic.data.docs

import com.medvedev.mechanic.domain.document.DocumentIds

object DocumentCatalog {

    private const val URL_FUEL_NORMS =
        "https://transtekhnika.by/uslugi/razrabotka-norm-rashoda-topliva/" +
                "%D0%9F%D0%BE%D1%81%D1%82%D0%B0%D0%BD%D0%BE%D0%B2%D0%BB%D0%B5%D0%BD%D0%B8%D0%B5%20" +
                "%D0%9C%D0%B8%D0%BD%D1%82%D1%80%D0%B0%D0%BD%D1%81%D0%B0%20N%2044%20%D0%BE%D1%82%20" +
                "01.08.2019.pdf"

    private const val URL_RESOLUTION_470 =
        "https://minfin.gov.by/upload/kru/acts/postsm/postsm_100820_470.pdf"

    fun urlFor(id: String): String? = when (id) {
        DocumentIds.FUEL_NORMS -> URL_FUEL_NORMS
        DocumentIds.RESOLUTION_470 -> URL_RESOLUTION_470
        else -> null
    }
}
