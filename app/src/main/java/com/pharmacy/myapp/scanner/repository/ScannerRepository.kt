package com.pharmacy.myapp.scanner.repository

import com.pharmacy.myapp.data.local.SPManager

class ScannerRepository(private val spManager: SPManager, private val srds: ScannerRemoteDataSource) {

    fun isQrCodeDescriptionShown(): Boolean = spManager.qrCodeDescriptionShown ?: false

    fun setDescriptionShown() {
        spManager.qrCodeDescriptionShown = true
    }

    suspend fun searchQrCode(code: String) = srds.searchBarcode(barCode = code)
}