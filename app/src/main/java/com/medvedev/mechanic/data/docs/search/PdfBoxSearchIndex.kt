package com.medvedev.mechanic.data.docs.search

import com.medvedev.mechanic.domain.document.PdfSearchIndex
import com.medvedev.mechanic.domain.model.PdfNormalizedRect
import com.medvedev.mechanic.domain.model.PdfSearchMatch
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import com.tom_roush.pdfbox.text.TextPosition
import kotlin.math.abs

private const val HIGHLIGHT_PAD_X_FRACTION = 0.04f
private const val HIGHLIGHT_PAD_Y_FRACTION = 0.4f
private const val HIGHLIGHT_EXTRA_TOP_FRACTION = 0.15f
private const val SAME_LINE_TOP_FRACTION = 0.6f

internal class PdfBoxSearchIndex private constructor(
    private val pages: List<PageGlyphs>,
) : PdfSearchIndex {
    override fun search(query: String): List<PdfSearchMatch> {
        val needle = query.trim().lowercase()
        if (needle.isEmpty()) return emptyList()
        return pages.flatMap { it.findMatches(needle) }
    }

    companion object {
        fun from(document: PDDocument): PdfBoxSearchIndex =
            PdfBoxSearchIndex(
                (0 until document.numberOfPages).map { index ->
                    val page = document.getPage(index)
                    val box = page.cropBox
                    val stripper = GlyphStripper()
                    stripper.startPage = index + 1
                    stripper.endPage = index + 1
                    stripper.getText(document)
                    val first = stripper.glyphs.firstOrNull()
                    pageGlyphs(
                        pageIndex = index,
                        pageWidth = first?.pageWidth ?: box.width.coerceAtLeast(1f),
                        pageHeight = first?.pageHeight ?: box.height.coerceAtLeast(1f),
                        glyphs = stripper.glyphs,
                    )
                },
            )
    }
}

private class GlyphStripper : PDFTextStripper() {
    val glyphs = mutableListOf<PdfGlyph>()

    init {
        sortByPosition = true
    }

    override fun writeString(text: String?, textPositions: MutableList<TextPosition>?) {
        if (textPositions.isNullOrEmpty()) return
        for (pos in textPositions) {
            val unicode = pos.unicode.orEmpty()
            if (unicode.isEmpty()) continue
            val height = pos.heightDir.coerceAtLeast(1f)
            glyphs += PdfGlyph(
                text = unicode,
                left = pos.xDirAdj,
                top = pos.yDirAdj - height,
                width = pos.widthDirAdj.coerceAtLeast(1f),
                height = height,
                pageWidth = pos.pageWidth.coerceAtLeast(1f),
                pageHeight = pos.pageHeight.coerceAtLeast(1f),
            )
        }
    }
}

private data class PdfGlyph(
    val text: String,
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float,
    val pageWidth: Float,
    val pageHeight: Float,
)

private fun pageGlyphs(
    pageIndex: Int,
    pageWidth: Float,
    pageHeight: Float,
    glyphs: List<PdfGlyph>,
): PageGlyphs {
    val haystack = StringBuilder()
    val glyphAtChar = ArrayList<Int>(glyphs.size)
    glyphs.forEachIndexed { glyphIndex, glyph ->
        val lower = glyph.text.lowercase()
        haystack.append(lower)
        repeat(lower.length) { glyphAtChar += glyphIndex }
    }
    return PageGlyphs(
        pageIndex = pageIndex,
        pageWidth = pageWidth,
        pageHeight = pageHeight,
        glyphs = glyphs,
        haystack = haystack.toString(),
        glyphAtChar = glyphAtChar.toIntArray(),
    )
}

private class PageGlyphs(
    val pageIndex: Int,
    val pageWidth: Float,
    val pageHeight: Float,
    val glyphs: List<PdfGlyph>,
    val haystack: String,
    val glyphAtChar: IntArray,
) {
    fun findMatches(needle: String): List<PdfSearchMatch> {
        if (haystack.isEmpty()) return emptyList()
        return buildList {
            var start = 0
            while (true) {
                val found = haystack.indexOf(needle, start)
                if (found < 0) break
                val end = found + needle.length
                val firstGlyph = glyphAtChar[found]
                val lastGlyph = glyphAtChar[end - 1]
                add(
                    PdfSearchMatch(
                        pageIndex = pageIndex,
                        rects = mergeGlyphRects(
                            glyphs.subList(firstGlyph, lastGlyph + 1),
                            pageWidth,
                            pageHeight,
                        ),
                        pageWidth = pageWidth,
                        pageHeight = pageHeight,
                    ),
                )
                start = end
            }
        }
    }
}

private fun mergeGlyphRects(
    glyphs: List<PdfGlyph>,
    pageWidth: Float,
    pageHeight: Float,
): List<PdfNormalizedRect> {
    if (glyphs.isEmpty()) return emptyList()
    val lines = mutableListOf<MutableList<PdfGlyph>>()
    for (glyph in glyphs) {
        val last = lines.lastOrNull()?.lastOrNull()
        val sameLine =
            last != null && abs(glyph.top - last.top) < last.height * SAME_LINE_TOP_FRACTION
        if (sameLine) {
            lines.last().add(glyph)
        } else {
            lines += mutableListOf(glyph)
        }
    }
    return lines.map { line ->
        val left = line.minOf { it.left }
        val top = line.minOf { it.top }
        val right = line.maxOf { it.left + it.width }
        val bottom = line.maxOf { it.top + it.height }
        val padX = (right - left) * HIGHLIGHT_PAD_X_FRACTION
        val padY = (bottom - top) * HIGHLIGHT_PAD_Y_FRACTION
        val extraTop = (bottom - top) * HIGHLIGHT_EXTRA_TOP_FRACTION
        PdfNormalizedRect(
            left = ((left - padX) / pageWidth).coerceIn(0f, 1f),
            top = ((top - padY - extraTop) / pageHeight).coerceIn(0f, 1f),
            width = ((right - left + 2 * padX) / pageWidth).coerceAtLeast(0f),
            height = ((bottom - top + 2 * padY + extraTop) / pageHeight).coerceAtLeast(0f),
        )
    }
}
