package com.medvedev.mechanic.domain.model

data class PdfSearchMatch(
    val pageIndex: Int,
    val rects: List<PdfNormalizedRect>,
    val pageWidth: Float,
    val pageHeight: Float,
)
