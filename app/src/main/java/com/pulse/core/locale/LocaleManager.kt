package com.pulse.core.locale

import android.content.Context
import com.pulse.core.utils.SingletonHolder
import com.pulse.data.local.SPManager
import java.util.*

class LocaleManager private constructor(private val sp: SPManager) : ILocaleManager {

    private val appLocale
        get() = sp.locale

    init {
        Locale.setDefault(Locale(appLocale.language))
    }

    override fun createLocalisedContext(context: Context): Context = Locale(appLocale.language).let {
        Locale.setDefault(it)
        context.createConfigurationContext(context.resources.configuration.apply {
            setLocale(it)
            setLayoutDirection(it)
        })
    }

    override val applicationLocale: Locale
        get() = Locale(appLocale.language)

    companion object : SingletonHolder<LocaleManager, SPManager>(::LocaleManager)
}