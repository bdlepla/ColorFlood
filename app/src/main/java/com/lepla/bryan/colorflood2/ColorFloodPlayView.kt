package com.lepla.bryan.colorflood2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class ColorFloodPlayView: View {
    constructor(context: Context): super(context)
    constructor(context:Context, attrs: AttributeSet): super(context, attrs)
    constructor(context:Context, attrs: AttributeSet, defStyleAttr: Int):
            super(context, attrs, defStyleAttr)

    private val paints = listOf(
        Color.RED,   Color.YELLOW, Color.BLUE,
        Color.GREEN, Color.LTGRAY, Color.DKGRAY)
        .map(::createPaint)

    private var rows = 9
    private var cols = 7
    //private val colors = paints.count()
    //private var data = (1..rows).map { (1..cols).map { (Math.random()*colors.toFloat()).toInt() } }
    private var data = (1..rows).map { (1..cols).map { 1 } }
    private var blockWidth = 1
    private var blockHeight = 1
    private val subscriptions = CompositeDisposable()

    fun initialize(whenBoardChanges: Observable<String>) {
        subscriptions.add(whenBoardChanges.subscribe(::updateBoard))
    }

    override fun onDetachedFromWindow() {
        subscriptions.dispose()
        super.onDetachedFromWindow()
    }

    private fun updateBoard(newBoardRep: String) {
        if (newBoardRep.isNotEmpty()) {
            val (r, c, _, board) = decode(newBoardRep)
            rows = r
            cols = c
            data = board.to2dIntList(c)
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        blockWidth = w / cols
        blockHeight = h / rows
    }

    override fun onDraw(canvas: Canvas) {
        (0 until rows).forEach {row ->
            (0 until cols).forEach {col ->
                canvas.drawRect(col*blockWidth,
                    row*blockHeight,
                    (col+1)*blockWidth,
                    (row+1)*blockHeight,
                    paints[data[row][col]] )
            }
        }
    }
}