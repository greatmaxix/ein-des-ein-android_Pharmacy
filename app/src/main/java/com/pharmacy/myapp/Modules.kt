package com.pharmacy.myapp

import androidx.work.WorkManager
import com.pharmacy.myapp.auth.authModule
import com.pharmacy.myapp.cart.cartModule
import com.pharmacy.myapp.chat.chatModule
import com.pharmacy.myapp.checkout.checkoutModule
import com.pharmacy.myapp.checkoutMap.checkoutMapModule
import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.devTools.devToolsModule
import com.pharmacy.myapp.home.homeModule
import com.pharmacy.myapp.main.mainModule
import com.pharmacy.myapp.order.orderModule
import com.pharmacy.myapp.payments.paymentsModule
import com.pharmacy.myapp.productCard.productCardModule
import com.pharmacy.myapp.profile.profileModule
import com.pharmacy.myapp.qrCodeScanner.qrCodeScannerModule
import com.pharmacy.myapp.search.searchModule
import com.pharmacy.myapp.splash.splashModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object Modules {

    fun getListOfModules() = mutableListOf(
        managerModule,
        devToolsModule,
        mainModule,
        splashModule,
        profileModule,
        homeModule,
        authModule,
        qrCodeScannerModule,
        productCardModule,
        checkoutModule,
        orderModule,
        searchModule,
        cartModule,
        chatModule,
        checkoutMapModule,
        paymentsModule
    )

    private val managerModule = module(true) {
        single { SPManager(androidApplication()) }
        single { RestManager() }
        single { WorkManager.getInstance(androidApplication()) }
        single { DBManager(androidApplication()) }
    }
}