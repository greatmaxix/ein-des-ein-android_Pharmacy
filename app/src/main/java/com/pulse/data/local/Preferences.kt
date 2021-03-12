package com.pulse.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Preferences {

    object Token {
        private const val TOKEN = "token"
        val FIELD_ACCESS_TOKEN = stringPreferencesKey(TOKEN)

        private const val REFRESH_TOKEN = "refresh_token"
        val FIELD_REFRESH_TOKEN = stringPreferencesKey(REFRESH_TOKEN)
    }

    object Locale {
        private const val LOCALE = "locale"
        val FIELD_LOCALE = stringPreferencesKey(LOCALE)
    }

    object BarCodeDescription {
        private const val BAR_CODE_DESCRIPTION_SHOWN = "barcode_description_shown"
        val FIELD_BAR_CODE_DESCRIPTION_SHOWN = booleanPreferencesKey(BAR_CODE_DESCRIPTION_SHOWN)
    }

    object Onboarding {
        private const val IS_ONBOARDING_SHOWN = "is_onboarding_shown"
        val FIELD_IS_ONBOARDING_SHOWN = booleanPreferencesKey(IS_ONBOARDING_SHOWN)
    }

    object Region {
        private const val IS_REGION_SELECTED = "is_region_selected"
        val FIELD_IS_REGION_SELECTED = booleanPreferencesKey(IS_REGION_SELECTED)

        private const val REGION_ID = "region_id"
        val FIELD_REGION_ID = intPreferencesKey(REGION_ID)
    }

    object Chat {
        private const val OPENED_CHAT_ID = "opened_chat_id"
        val FIELD_OPENED_CHAT_ID = intPreferencesKey(OPENED_CHAT_ID)

        private const val IS_CHAT_FOREGROUND = "is_chat_foreground"
        val FIELD_IS_CHAT_FOREGROUND = booleanPreferencesKey(IS_CHAT_FOREGROUND)
    }

}