package com.pulse.components.search.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.getOnes
import com.pulse.data.local.Preferences.Region.FIELD_REGION_ID

class SearchRepository(private val dataStore: DataStore<Preferences>, private val rds: SearchRemoteDataSource) {

    suspend fun searchPaging(productName: String?, page: Int = 1, pageSize: Int = 10, categoryCode: String? = null) =
        rds.globalSearch(productName, page, pageSize, categoryCode, dataStore.getOnes(FIELD_REGION_ID))
}