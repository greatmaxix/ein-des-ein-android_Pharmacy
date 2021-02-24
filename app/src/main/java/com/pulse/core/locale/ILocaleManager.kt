package com.pulse.core.locale

import android.content.Context
import java.util.*

interface ILocaleManager {

    fun createLocalisedContext(context: Context): Context

    val applicationLocale: Locale
}