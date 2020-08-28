package com.pharmacy.myapp.scanner.repository

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager

class ScannerRepository(private val spManager: SPManager, private val rm: RestManager) {

    fun isQrCodeDescriptionShown(): Boolean = spManager.qrCodeDescriptionShown ?: false

    fun setDescriptionShown() {
        spManager.qrCodeDescriptionShown = true
    }

    fun searchQrCode(code: String) {
        // TODO implement code search
    }
}