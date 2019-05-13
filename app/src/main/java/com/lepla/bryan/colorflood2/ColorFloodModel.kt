package com.lepla.bryan.colorflood2

object ColorFloodModel {
    fun applyColor(rep: String, newColor: Int): String {
        if (rep.length < 5) return rep

        val (rows, cols, colors, theRest) = decode(rep)
        if (theRest.length != rows * cols) return rep

        val rowRange = 0 until rows
        val colRange = 0 until cols

        val intRows = theRest.to2dIntList(cols)
        val firstColor = intRows[0][0]
        if (firstColor == newColor) return rep

        fun List<MutableList<Int>>.doApplyColor(row: Int, col: Int) {
            if (row in rowRange && col in colRange && this[row][col] == firstColor) {

                this[row][col] = newColor

                doApplyColor(row - 1, col)
                doApplyColor(row + 1, col)
                doApplyColor(row, col - 1)
                doApplyColor(row, col + 1)
            }
        }

        intRows.doApplyColor(0, 0)
        return encode(intRows, colors)
    }

    fun isCompleted(rep: String) = rep.drop(6).all{it == rep[5]}
}