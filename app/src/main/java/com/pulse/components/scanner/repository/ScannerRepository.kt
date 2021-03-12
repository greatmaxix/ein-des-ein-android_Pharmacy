package com.pulse.components.scanner.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pulse.core.extensions.getOnes
import com.pulse.core.extensions.put
import com.pulse.data.local.Preferences.BarCodeDescription.FIELD_BAR_CODE_DESCRIPTION_SHOWN

class ScannerRepository(private val dataStore: DataStore<Preferences>, private val srds: ScannerRemoteDataSource) {

    fun isQrCodeDescriptionShown(): Boolean = dataStore.getOnes(FIELD_BAR_CODE_DESCRIPTION_SHOWN, false)

    suspend fun setDescriptionShown() = dataStore.put(FIELD_BAR_CODE_DESCRIPTION_SHOWN, true)

    suspend fun searchQrCode(code: String) = srds.searchBarcode(barCode = code)
}