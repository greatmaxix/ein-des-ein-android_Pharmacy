package com.pulse.core.locale

import androidx.annotation.StringRes
import com.pulse.R

enum class LocaleEnum(val code: String, val language: String, @StringRes val titleResId: Int) {

    RU("ru", "ru", R.string.language_ru),
    UA("uk", "uk", R.string.language_ua),
    KZ("kk", "kk", R.string.language_kz),
    UZ("uz", "uz", R.string.language_uz),
    EN("en", "en", R.string.language_en);

    companion object {

        fun getLocale(language: String) = values().find { it.language == language } ?: RU
    }
}