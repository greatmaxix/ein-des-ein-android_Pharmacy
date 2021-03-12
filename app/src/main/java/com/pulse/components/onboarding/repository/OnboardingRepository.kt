package com.pulse.components.onboarding.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.Region.FIELD_IS_REGION_SELECTED

class OnboardingRepository(private val dataStore: DataStore<Preferences>) {

    suspend fun setRegionSelectionFlag() = dataStore.put(FIELD_IS_REGION_SELECTED, true)
}