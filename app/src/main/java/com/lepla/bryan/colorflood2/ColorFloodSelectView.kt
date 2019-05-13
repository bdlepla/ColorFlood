package com.lepla.bryan.colorflood2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class ColorFloodSelectView : View {
    constructor(context: Context): super(context)
    constructor(context:Context, attrs: AttributeSet): super(context, attrs)
    constructor(context:Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    private val paints = listOf(
        Color.RED, Color.YELLOW, Color.BLUE,
        Color.GREEN, Color.LTGRAY, Color.DKGRAY)
        .map(::createPaint)

    private var radius = Math.min(width, height) / 2

    fun whenColorClicked(): Observable<Int> = this.touchDown()
        .observeOn(AndroidSchedulers.mainThread())
        .map {
            when {
                isPortrait() -> it.x * paintCount / width
                else -> paintCount - (it.y * paintCount / height) // because painting is reversed in landscape
            }.toInt()
        }

    private fun isPortrait() = width > height // this is comparing the size of this view, not the whole screen
    private var paintCount = paints.count()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        radius = when {
            isPortrait() -> Math.min(width / paintCount, height)/2
            else         -> Math.min(height / paintCount, width)/2
        }
    }

    override fun onDraw(c: Canvas) {
        val r = radius
        val d = r * 2
        if (isPortrait()) {
            val cy = height / 2
            paints.forEachIndexed { i, p -> c.drawCircle(r + i * d , cy, r, p) }
        } else {
            val cx = width / 2
            paints.reversed().forEachIndexed { i, p -> c.drawCircle(cx, r + i * d, r, p) }
        }
    }
}