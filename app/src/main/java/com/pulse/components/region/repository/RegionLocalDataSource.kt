package com.pulse.components.region.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.Region.FIELD_REGION_ID
import com.pulse.model.region.LocalRegion
import com.pulse.model.region.RegionDAO

class RegionLocalDataSource(private val dao: RegionDAO, private val dataStore: DataStore<Preferences>) {

    fun clear() = dao.clear()

    suspend fun setRegion(region: LocalRegion) = dao.insert(region)

    fun getTemporaryRegion() = dao.get()

    suspend fun setLocalRegion(region: Int) = dataStore.put(FIELD_REGION_ID, region)
}