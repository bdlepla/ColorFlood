package com.lepla.bryan.colorflood2

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class StringTests : StringSpec({

    "Groups of Test" {
        val str = "012345678901"
        val expected = str.chunksOf(3)
        expected.count().shouldBe(4)
        expected[0].shouldBe("012")
        expected[2].shouldBe("678")
    }

    "StringTo2dArrayAndBack" {
        val str = "012345678901234"
        val wow = str.to2dIntList(5)
        wow[0].shouldBe(listOf(0, 1, 2, 3, 4))
        val actual = wow.joinToString()
        actual.shouldBe(str)
    }

    "MyTest" {
        val num = 3
        val numStr = num.toString()
        val actualNum = numStr.toInt()
        actualNum.shouldBe(num)

        val numList = listOf(3, 4, 5, 6, 7)
        val numStr2 = numList.joinToString("")
        numStr2.shouldBe("34567")

        val backToList = numStr2.map { it.toString().toInt() }
        backToList.shouldBe(numList)
    }
})