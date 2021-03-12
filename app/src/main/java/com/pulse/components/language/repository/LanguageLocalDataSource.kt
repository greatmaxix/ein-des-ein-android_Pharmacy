package com.pulse.components.language.repository

import com.pulse.core.locale.ILocaleManager
import com.pulse.core.locale.LocaleEnum

class LanguageLocalDataSource(private val localeManager: ILocaleManager) {

    fun setLocale(locale: LocaleEnum) {
        localeManager.appLocale = locale
    }

    val appLocale
        get() = localeManager.appLocale
}