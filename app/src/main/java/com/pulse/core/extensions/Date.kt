package com.pulse.core.extensions

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val String.dateFormatRecipes
    get() = try {
        val formatInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)
        val formatOutput = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        formatInput.parse(this)?.let(formatOutput::format) ?: ""
    } catch (e: ParseException) {
        Timber.e(e)
        ""
    }