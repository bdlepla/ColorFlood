package com.lepla.bryan.colorflood2

import org.junit.Assert.assertEquals
import org.junit.Test

class StringTests {
    @Test
    fun `Groups of Test`() {
        val str = "012345678901"
        val actual = str.chunksOf(3)
        val expected = 4
        assertEquals(expected, actual.count())

        val actual0 = actual[0]
        val expected0 = "012"
        assertEquals(expected0, actual0)

        val actual1 = actual[1]
        val expected1 = "345"
        assertEquals(expected1, actual1)
    }

    @Test
    fun `String To 2d Array And Back`() {
        val str = "012345678901234"
        val twoDIntList = str.to2dIntList(5)

        val actual1 = twoDIntList[1].joinToString("")
        val expected1 = "56789"
        assertEquals(expected1, actual1)

        val actual = twoDIntList.joinToString()
        assertEquals(str, actual)
    }

}