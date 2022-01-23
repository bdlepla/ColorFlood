package com.lepla.bryan.colorflood2

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.jakewharton.rxbinding2.view.touches
import io.reactivex.Observable

fun View.touchDown(): Observable<MotionEvent> = this.touches().filter(MotionEvent::isDown)
private fun MotionEvent.isAction(actionToCheck: Int) =
    (action and MotionEvent.ACTION_MASK) == actionToCheck

fun MotionEvent.isDown() = isAction(MotionEvent.ACTION_DOWN)

fun String.chunksOf(number:Int): List<String> {
    fun String.doChunksOf(chunkSize: Int): List<String> {
        return when (val num = kotlin.math.min(chunkSize, this.length)) {
            0 -> emptyList()
            else -> listOf(this.take(num)) + this.drop(num).doChunksOf(num)
        }
    }
    return this.doChunksOf(number)
}

fun String.to2dIntList(groups:Int) =
    this.chunksOf(groups).map { group -> group.map { it.toString().toInt() }.toMutableList() }

fun List<List<Int>>.joinToString() = joinToString("") { it.joinToString("") }

data class DecodedString(val rows:Int, val cols: Int, val colors: Int, val rep: String)

fun decode(rep: String) = DecodedString(rep.substring(0..1).toInt(),
    rep.substring(2..3).toInt(), rep.substring(4..4).toInt(), rep.substring(5))

fun encode(data:List<List<Int>>, colors: Int) :String {
    val twoDigits = "%02d"
    return twoDigits.format(data.size) + twoDigits.format(data[0].size) +
            colors.toString() + data.joinToString()
}

fun String?.doIfEmpty(block: () -> String) =
    if ((this == null) or (this == "")) block() else this!!

fun createPaint(withColor: Int) = Paint(0).apply { color = withColor }

fun Canvas.drawRect(left: Int, top: Int, right: Int, bottom: Int, paint: Paint) =
    drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)

fun Canvas.drawCircle(centerX:Int, centerY:Int, radius:Int, paint: Paint) =
    drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paint)
