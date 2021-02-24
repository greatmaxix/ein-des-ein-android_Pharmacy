package com.pulse.components.language.repository

import com.pulse.core.locale.LocaleEnum
import com.pulse.data.local.SPManager

class LanguageLocalDataSource(val sp: SPManager) {

    val selectedLocale
        get() = sp.locale

    fun setLocale(locale: LocaleEnum) {
        sp.locale = locale
    }
}