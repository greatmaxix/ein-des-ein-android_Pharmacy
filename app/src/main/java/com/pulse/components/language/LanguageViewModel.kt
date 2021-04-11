package com.pulse.components.language

import com.pulse.components.language.model.LanguageAdapterModel
import com.pulse.components.language.repository.LanguageRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.locale.LocaleEnum
import com.pulse.core.utils.flow.StateEventFlow
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class LanguageViewModel(private val repository: LanguageRepository) : BaseViewModel() {

    private val selectedLocale = repository.appLocale
    val languageState = StateEventFlow(
        LocaleEnum.values().map {
            LanguageAdapterModel(it, selectedLocale == it)
        }
    )

    fun setLanguage(locale: LocaleEnum) = repository.setLocale(locale)
}