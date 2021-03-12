package com.pulse.components.splash.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.getOnes
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.Onboarding.FIELD_IS_ONBOARDING_SHOWN
import com.pulse.data.local.Preferences.Region.FIELD_IS_REGION_SELECTED

class SplashRepository(private val dataStore: DataStore<Preferences>) {

    val isNeedRegionSelection: Boolean
        get() = dataStore.getOnes(FIELD_IS_REGION_SELECTED, false)

    val isNeedOnboarding: Boolean
        get() = dataStore.getOnes(FIELD_IS_ONBOARDING_SHOWN, true)

    suspend fun setIsNeedOnboarding(isNeedOnboarding: Boolean) {
        dataStore.put(FIELD_IS_ONBOARDING_SHOWN, isNeedOnboarding)
    }
}