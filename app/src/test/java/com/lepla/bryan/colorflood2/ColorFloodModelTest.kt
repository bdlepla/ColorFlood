package com.lepla.bryan.colorflood2

import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
/**
 * Created by bryan on 7/23/17.
 *
 */

class ColorFloodModelTest : StringSpec({
    "test" {
        val str = "020230011"
        val actual = ColorFloodModel.applyColor(str, 1)
        actual.shouldBe("020231111")

        val str2 = "02032000111"
        val act2 = ColorFloodModel.applyColor(str2, 1)
        act2.shouldBe("02032111111")

        // 01
        // 10
        // 01
        val str3 = "03023011001"
        val act3 = ColorFloodModel.applyColor(str3, 1)
        // 11
        // 10
        // 01
        act3.shouldBe("03023111001")

        // 011
        // 001
        val str4 = "02033011001"
        val act4 = ColorFloodModel.applyColor(str4, 1)
        // 111
        // 111
        act4.shouldBe("02033111111")

        // 0122
        // 0122
        // 0122
        val str5 = "03043012201220122"
        val act5 = ColorFloodModel.applyColor(str5, 2)
        // 2122
        // 2122
        // 2122
        act5.shouldBe("03043212221222122")

    }

    "TestIsCompleted" {
        // 01122212101020102021
        val start = "0405301122212101020102021"
        ColorFloodModel.isCompleted(start).shouldBeFalse()
        val finish = "040531111111111111111111"
        ColorFloodModel.isCompleted(finish).shouldBeTrue()
    }
})