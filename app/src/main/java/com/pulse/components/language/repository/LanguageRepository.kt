package com.pulse.components.language.repository

import com.pulse.core.locale.LocaleEnum
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@KoinApiExtension
class LanguageRepository(private val rds: LanguageRemoteDataSource, private val lds: LanguageLocalDataSource) : KoinComponent {

    fun setLocale(locale: LocaleEnum) {
        lds.setLocale(locale)
    }

    val appLocale
        get() = lds.appLocale
}