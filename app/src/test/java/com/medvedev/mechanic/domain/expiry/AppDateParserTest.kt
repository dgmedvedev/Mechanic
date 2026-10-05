package com.medvedev.mechanic.domain.expiry

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class AppDateParserTest {

    @Test
    fun parsesValidDate() {
        assertEquals(LocalDate.of(2027, 6, 1), AppDateParser.parse("01.06.2027"))
    }

    @Test
    fun emptyOrBlankIsNull() {
        assertNull(AppDateParser.parse(""))
        assertNull(AppDateParser.parse("   "))
    }

    @Test
    fun invalidDateIsNull() {
        assertNull(AppDateParser.parse("32.13.2020"))
        assertNull(AppDateParser.parse("2027-06-01"))
        assertNull(AppDateParser.parse("not a date"))
    }
}
