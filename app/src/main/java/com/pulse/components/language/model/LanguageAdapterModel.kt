package com.pulse.components.language.model

import com.pulse.core.locale.LocaleEnum

data class LanguageAdapterModel(
    val language: LocaleEnum,
    var isSelected: Boolean
)