package com.pulse.core.locale

import androidx.annotation.StringRes
import com.pulse.R

enum class LocaleEnum(val code: String, val language: String, @StringRes val titleResId: Int) {

    RU("ru", "ru", R.string.language_ru),
    UA("ua", "ua", R.string.language_ua),
    KZ("kz", "kz", R.string.language_kz),
    EN("en", "en", R.string.language_en);

    companion object {

        fun getAppLocale(language: String) = values().find { it.language == language } ?: RU
    }
}