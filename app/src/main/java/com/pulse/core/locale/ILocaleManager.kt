package com.pulse.core.locale

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface ILocaleManager {

    fun createLocalisedContext(context: Context): Context

    val appLocaleFlow: Flow<LocaleEnum>

    var appLocale: LocaleEnum
}