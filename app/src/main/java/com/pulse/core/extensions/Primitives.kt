package com.pulse.core.extensions

import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.annotation.ColorInt
import com.pulse.R
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode
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

fun String.bold() = SpannableString(this).apply {}

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

fun String.formatPrice(): CharSequence {
    if (!contains("\\.".toRegex())) {
        return this
    }

    val split = split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
    val firstSplitPart = SpannableString(split.first())
    val secondSplitPart = SpannableString(split.last())

    if (secondSplitPart.length < 3) {
        return this
    }

    val startPosition = if (secondSplitPart.length == 2) 0 else secondSplitPart.length - 3
    val endPosition = if (secondSplitPart.length == 2) secondSplitPart.length else secondSplitPart.length - 1
    secondSplitPart.setSpan(RelativeSizeSpan(1.25f), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    secondSplitPart.setSpan(StyleSpan(Typeface.BOLD), startPosition, endPosition, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
    return TextUtils.concat(firstSplitPart, ".", secondSplitPart)
}

val String.firstNumber: String get() = Pattern.compile("\\d+").matcher(this).apply { find() }.group()

fun String.getAllNumbers() = replace("\\D+".toRegex(), "")

fun String.withPad() = padStart(length + 1)

fun String.formatPhone(): String {
    val pattern = "(\\D\\d)(\\d{3})(\\d{3})(\\d{2})(\\d+)"
    return replaceFirst(Regex(pattern), "$1 ($2) $3-$4-$5")
}

fun String.addPlusSignIfNeeded() = if (contains("+")) this else "+".plus(this)

fun String.spanSearchCount(count: Int) = SpannableString(this).apply {
    setSpan(StyleSpan(Typeface.BOLD), 8, 8 + count.length(), 0)
    setSpan(RelativeSizeSpan(1.2F), 8, 8 + count.length(), 0)
}

val Float.isPositive get() = this > 0

fun Float.toSymbol(needMinus: Boolean = false) = if (isPositive) "+" else if (needMinus) "-" else ""

val Float.toPercent get() = abs(this) * 100

fun Float.formatPrice(digits: Int = 2): String = String.format(Locale.US, "%.${digits}f", this)

fun Double.formatPrice(digits: Int = 2): String = String.format(Locale.US, "%.${digits}f", this)

fun BigDecimal.more(bigDecimal: BigDecimal?) = compareTo(bigDecimal) > 0

fun BigDecimal.less(bigDecimal: BigDecimal?) = compareTo(bigDecimal) < 0

fun BigDecimal.equal(bigDecimal: BigDecimal?) = compareTo(bigDecimal) == 0

val BigDecimal.isZero get() = equal(ZERO)

fun BigDecimal.formatPrice(digits: Int): String = String.format(Locale.US, "%.${digits}f", this)

val BigDecimal.isPositive get() = more(ZERO) || equal(ZERO)

fun BigDecimal.scaleDown(scale: Int = 2): BigDecimal = setScale(scale, RoundingMode.DOWN)// https://youtu.be/zmqhe7fDEHI?t=2495

fun BigDecimal.scaleUp(scale: Int = 2): BigDecimal = setScale(scale, RoundingMode.UP)

fun BigDecimal.toSymbol(needMinus: Boolean = false) = if (isPositive) "+" else if (needMinus) "-" else ""

fun BigDecimal.divideScale(value: BigDecimal): BigDecimal = divide(value, 0, RoundingMode.HALF_DOWN)//.scaleUp(0)

fun BigDecimal?.numberRangeType(min: BigDecimal?, max: BigDecimal?) =
    this?.let {
        when {
            equal(min) && equal(max) -> Range.EQUAL
            less(min) -> Range.MIN
            more(max) -> Range.MAX
            more(min) || less(max) -> Range.IN
            else -> Range.NONE
        }
    } ?: Range.NONE

fun Int.mixColorWith(@ColorInt color: Int, ratio: Float): Int {
    val inverseRatio = 1f - ratio

    val r = (Color.red(color) * ratio + Color.red(this) * inverseRatio).toInt()
    val b = (Color.blue(color) * ratio + Color.blue(this) * inverseRatio).toInt()
    val g = Color.green(color) * ratio + Color.green(this) * inverseRatio

    return Color.rgb(r, g.toInt(), b)
}

fun Int.length() = when (this) {
    0 -> 1
    else -> log10(abs(toDouble())).toInt() + 1
}

enum class Range {
    MIN, MAX, IN, EQUAL, NONE
}
