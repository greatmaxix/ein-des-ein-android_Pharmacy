package com.pharmacy.myapp.utils.extension

import android.text.Editable
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.isPositive
import com.pharmacy.myapp.core.extensions.numberRangeType
import com.pharmacy.myapp.core.extensions.scaleUp
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun String.round(newScale: Int = 2) = toBigDecimal().scaleUp(newScale).toString()

val String.plusOrMinus get() = if (isPositive) "+" else "-"

val String.isPositive get() = trim().first() != '-'

fun String.isPositive(isMinusFirst: Boolean = true) = if (isMinusFirst) {
    isPositive
} else !contains("-")

fun BigDecimal.toStyledAmount(abs: Boolean = true, digits: Int = 2): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US) as DecimalFormat
    format.maximumFractionDigits = digits
    format.decimalFormatSymbols = format.decimalFormatSymbols.apply { currencySymbol = "" }
    return format.format(if (abs) abs() else this)
}

fun BigDecimal.negateOrReturn(isNeedMinus: Boolean, scale: Int = 2) = if (isNeedMinus) {
    negate()
} else {
    this
}.scaleUp(scale)

fun BigDecimal.minusSymbol(): String {
    return if(!isPositive) "-" else ""
}

fun Editable?.decimalOr(value: BigDecimal? = null) = toString().replace(",", ".").replace(" ", "").run { if (isNullOrEmpty()) value else toString().toBigDecimal() }

fun Editable?.rangeType(min: BigDecimal?, max: BigDecimal?) = decimalOr(BigDecimal.ZERO).numberRangeType(min, max)

val Boolean.inputTextBackground
    get() = if (this) R.color.colorBackgroundInputTextPrimary else R.color.colorBackgroundInputTextPrimaryDisable

val Boolean.inputTextColor
    get() = if (this) R.color.colorTextInputTextPrimary else R.color.colorTextInputTextPrimaryDisable

val Boolean.logoTopMargin
    get() = if (this) R.dimen._28sdp else R.dimen._98sdp
