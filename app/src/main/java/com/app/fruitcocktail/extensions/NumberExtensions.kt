package com.app.fruitcocktail.extensions

import android.content.res.Resources
import java.math.BigDecimal
import kotlin.math.round

fun Int.pxToDp(): Float = (this / Resources.getSystem().displayMetrics.density)
fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Int.pxToSp(): Float = (this / Resources.getSystem().displayMetrics.scaledDensity)
fun Int.spToPx(): Int = (this * Resources.getSystem().displayMetrics.scaledDensity).toInt()

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (round(this * multiplier) / multiplier).toFloat()
}