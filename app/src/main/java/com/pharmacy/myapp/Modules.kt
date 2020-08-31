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
import com.pharmacy.myapp.onboarding.onboardingModule
import com.pharmacy.myapp.order.orderModule
import com.pharmacy.myapp.payments.paymentsModule
import com.pharmacy.myapp.product.productCardModule
import com.pharmacy.myapp.user.profile.profileModule
import com.pharmacy.myapp.scanner.qrCodeScannerModule
import com.pharmacy.myapp.region.regionModule
import com.pharmacy.myapp.search.searchModule
import com.pharmacy.myapp.splash.splashModule
import com.pharmacy.myapp.user.userModule
import com.pharmacy.myapp.user.wishlist.wishModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object Modules {

    fun getListOfModules() = mutableListOf(
        managerModule,
        devToolsModule,
        mainModule,
        splashModule,
        onboardingModule,
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
        regionModule,
        paymentsModule,
        userModule,
        wishModule
    )

    private val managerModule = module(true) {
        single { SPManager(androidApplication()) }
        single { RestManager() }
        single { WorkManager.getInstance(androidApplication()) }
        single { DBManager(androidApplication()) }
    }
}