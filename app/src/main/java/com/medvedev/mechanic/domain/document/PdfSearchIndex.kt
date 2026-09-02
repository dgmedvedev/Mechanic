package com.medvedev.mechanic.domain.document

import com.medvedev.mechanic.domain.model.PdfSearchMatch

interface PdfSearchIndex {
    fun search(query: String): List<PdfSearchMatch>
}
