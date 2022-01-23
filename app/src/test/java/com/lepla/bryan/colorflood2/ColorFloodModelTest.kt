package com.lepla.bryan.colorflood2

import org.junit.Assert.*
import org.junit.Test


/**
 * Created by bryan on 7/23/17.
 *
 */

class ColorFloodModelTest {
    @Test
    fun test() {
        val str = "020230011"
        val actual = ColorFloodModel.applyColor(str, 1)
        val expected = "020231111"
        assertEquals(expected, actual)


        val str2 = "02032000111"
        val act2 = ColorFloodModel.applyColor(str2, 1)
        val exp2 = "02032111111"
        assertEquals(exp2, act2)

        // 01
        // 10
        // 01
        val str3 = "03023011001"
        val act3 = ColorFloodModel.applyColor(str3, 1)
        val exp3 = "03023111001"
        // 11
        // 10
        // 01
        assertEquals(exp3, act3)

        // 011
        // 001
        val str4 = "02033011001"
        val act4 = ColorFloodModel.applyColor(str4, 1)
        val exp4 = "02033111111"
        // 111
        // 111
        assertEquals(exp4, act4)

        // 0122
        // 0122
        // 0122
        val str5 = "03043012201220122"
        val act5 = ColorFloodModel.applyColor(str5, 2)
        val exp5 = "03043212221222122"
        // 2122
        // 2122
        // 2122
        assertEquals(exp5, act5)

    }

    @Test
    fun testIsCompleted() {
        // 01122212101020102021
        val start = "0405301122212101020102021"
        val act1 = ColorFloodModel.isCompleted(start)
        assertFalse(act1)

        val finish = "040531111111111111111111"
        val act2 = ColorFloodModel.isCompleted(finish)
        assertTrue(act2)
    }
}