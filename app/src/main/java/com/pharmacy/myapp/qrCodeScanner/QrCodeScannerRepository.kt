package com.pharmacy.myapp.qrCodeScanner

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager

class QrCodeScannerRepository(
    private val spManager: SPManager,
    private val rm: RestManager
) {

    fun isQrCodeDescriptionShown(): Boolean = spManager.qrCodeDescriptionShown ?: false

    fun setDescriptionShown() {
        spManager.qrCodeDescriptionShown = true
    }

    fun searchQrCode(code: String) {
        // TODO implement code search
    }
}