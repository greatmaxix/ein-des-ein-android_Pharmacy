package com.pharmacy.util

import android.text.InputFilter
import android.text.Spanned

class CreditCardInputFilter : InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
        if (dest.trim().length > 19) return ""
        return if (source.length == 1 && (dstart == 4 || dstart == 9 || dstart == 14)) " $source" else source
    }
}