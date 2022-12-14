package com.pulse.core.extensions

import android.content.Context
import android.graphics.*
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toBitmap
import com.pulse.R
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.log10


fun Boolean?.falseIfNull() = this ?: false

val Boolean.inputTextBackground
    get() = if (this) R.color.colorBackgroundInputTextPrimary else R.color.colorBackgroundInputTextPrimaryDisable

val Boolean.inputTextColor
    get() = if (this) R.color.colorTextInputTextPrimary else R.color.colorTextInputTextPrimaryDisable

val Boolean.wishResId
    get() = if (this) R.drawable.ic_heart_fill else R.drawable.ic_heart_stroke

fun String.setColor(@ColorInt color: Int) = SpannableString(this).apply {
    setSpan(ForegroundColorSpan(color), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun String.underlineSpan() = SpannableString(this).apply {
    setSpan(UnderlineSpan(), 0, length, 0)
}

val String.wrapHtml
    get() = String(Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).run {
        CharArray(length).also {
            TextUtils.getChars(this, 0, length, it, 0)
        }
    })

fun String.isLetterAndSpace(): Boolean {
    if (this.isEmpty()) {
        return false
    }
    val chars = this.toCharArray()
    for (index in chars.indices) {
        val codePoint = Character.codePointAt(chars, index)
        if (!Character.isLetter(codePoint) && !Character.isSpaceChar(codePoint)) {
            return false
        }
    }
    return true
}

fun String.toSmsCodeMatcher(): Matcher = Pattern.compile("(\\d{4})").matcher(replace("\\s".toRegex(), ""))

val String.firstNumber: String get() = Pattern.compile("\\d+").matcher(this).apply { find() }.group()

fun String.getAllNumbers() = replace("\\D+".toRegex(), "")

fun String.withPad() = padStart(length + 1)

fun String.formatPhone(): String {
    val pattern = "(\\D\\d)(\\d{3})(\\d{3})(\\d{2})(\\d+)"
    return replaceFirst(Regex(pattern), "$1 ($2) $3-$4-$5")
}

fun String.spanSearchCount(count: Int) = SpannableString(this).apply {
    setSpan(StyleSpan(Typeface.BOLD), 8, 8 + count.length(), 0)
    setSpan(RelativeSizeSpan(1.2F), 8, 8 + count.length(), 0)
}

fun String.addPlusSignIfNeeded() = if (contains("+")) this else "+".plus(this)

val Float.isPositive get() = this > 0

fun Float.toSymbol(needMinus: Boolean = false) = if (isPositive) "+" else if (needMinus) "-" else ""

val Float.toPercent get() = abs(this) * 100

fun Float.formatPrice(digits: Int = 2): String = toDouble().formatPrice(digits)

fun Double.formatPrice(digits: Int = 2): String = String.format(Locale.US, "%.${digits}f", this)

fun String.toPrice() = "$this ???"

fun Int.toPrice() = "$this ???"

fun Double.toPriceFormat() = "${this.formatPrice()} ???"

fun Float.toPriceFormat() = "${this.formatPrice()} ???"

fun Int.mixColorWith(@ColorInt color: Int, ratio: Float): Int {
    val inverseRatio = 1f - ratio

    val r = (Color.red(color) * ratio + Color.red(this) * inverseRatio).toInt()
    val b = (Color.blue(color) * ratio + Color.blue(this) * inverseRatio).toInt()
    val g = (Color.green(color) * ratio + Color.green(this) * inverseRatio).toInt()

    return Color.rgb(r, g, b)
}

fun Int.length() = when (this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

@Deprecated("use library")
fun String.asBitmap(context: Context, paint: Paint): Bitmap? {
    var bitmap = context.getDrawable(R.drawable.ic_marker_icon)?.toBitmap() ?: return null
    bitmap = bitmap.copy(bitmap.config ?: Bitmap.Config.ARGB_8888, true)
    val bounds = Rect()
    paint.getTextBounds(this, 0, length, bounds)
    val centerX = (bitmap.width - bounds.width()) / 2f
    val centerY = (bitmap.height + bounds.height()) / 2.8f
    Canvas(bitmap).drawText(this, centerX, centerY, paint)
    return bitmap
}