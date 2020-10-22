package com.pulse.util

import android.text.InputFilter
import android.text.Spanned
import java.text.SimpleDateFormat
import java.util.*

class CreditCardExpiryInputFilter : InputFilter {

    private val currentYearLastTwoDigits = SimpleDateFormat("yy", Locale.US).format(Date())

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
        if (dest.toString().length == 5 || source.length > 1 || (dest.isNotEmpty() && dstart != dest.length)) return ""
        if (source.isEmpty()) {
            return source
        }
        val inputChar = source[0]
        if (dstart == 0) {
            if (inputChar > '1') return ""
        }
        if (dstart == 1) {
            val firstMonthChar = dest[0]
            if (firstMonthChar == '0' && inputChar == '0') return ""
            if (firstMonthChar == '1' && inputChar > '2') return ""
        }
        if (dstart == 2) {
            val currYearFirstChar = currentYearLastTwoDigits[0]
            return if (inputChar < currYearFirstChar) "" else "/$source"
        }
        if (dstart == 4) {
            val inputYear = "${dest[dest.length - 1]}$source"
            if (inputYear < currentYearLastTwoDigits) return ""
        }
        return source
    }
}