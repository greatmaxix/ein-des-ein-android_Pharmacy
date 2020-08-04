package com.pharmacy.myapp

import com.pharmacy.myapp.auth.authModule
import com.pharmacy.myapp.cart.cartModule
import com.pharmacy.myapp.checkout.checkoutModule
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.devTools.devToolsModule
import com.pharmacy.myapp.home.homeModule
import com.pharmacy.myapp.productCard.productCardModule
import com.pharmacy.myapp.profile.profileModule
import com.pharmacy.myapp.qrCodeScanner.qrCodeScannerModule
import com.pharmacy.myapp.splash.splashModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object Modules {

    fun getListOfModules() = mutableListOf(
        managerModule,
        devToolsModule,
        splashModule,
        profileModule,
        homeModule,
        authModule,
        qrCodeScannerModule,
        productCardModule,
        checkoutModule,
        cartModule
    )

    private val managerModule = module(true) {
        single { SPManager(androidApplication()) }
        single { RestManager() }
//        single { DBManager(androidApplication(), get()) }
    }
}