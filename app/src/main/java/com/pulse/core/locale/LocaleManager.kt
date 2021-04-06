package com.pulse.core.locale

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.get
import com.pulse.core.extensions.getOnes
import com.pulse.core.extensions.put
import com.pulse.core.utils.SingletonHolder
import com.pulse.data.local.Preferences.Locale.FIELD_LOCALE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.*

class LocaleManager private constructor(private val dataStore: DataStore<Preferences>) : ILocaleManager {

    private var _locale: LocaleEnum? = null

    override var appLocale: LocaleEnum
        get() {
            val value = _locale ?: LocaleEnum.getLocale(dataStore.getOnes(FIELD_LOCALE, LocaleEnum.getLocale(Locale.getDefault().language).language))
            if (_locale == null) {
                _locale = value
            }
            return value
        }
        set(value) {
            _locale = value
            runBlocking { dataStore.put(FIELD_LOCALE, value.language) }
        }

    override val appLocaleFlow: Flow<LocaleEnum> = dataStore.get(FIELD_LOCALE, LocaleEnum.getLocale(Locale.getDefault().language).language).map { LocaleEnum.getLocale(it) }

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

    companion object : SingletonHolder<ILocaleManager, DataStore<Preferences>>(::LocaleManager)
}