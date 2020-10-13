package com.pharmacy

import androidx.work.WorkManager
import com.pharmacy.auth.authModule
import com.pharmacy.cart.cartModule
import com.pharmacy.categories.categoriesModule
import com.pharmacy.chat.chatModule
import com.pharmacy.checkout.checkoutModule
import com.pharmacy.data.local.DBManager
import com.pharmacy.data.local.SPManager
import com.pharmacy.data.remote.RESTModule
import com.pharmacy.data.remote.RestManager
import com.pharmacy.devTools.devToolsModule
import com.pharmacy.home.homeModule
import com.pharmacy.main.mainModule
import com.pharmacy.onboarding.onBoardingModule
import com.pharmacy.orders.details.orderModule
import com.pharmacy.orders.ordersModule
import com.pharmacy.payments.paymentsModule
import com.pharmacy.pharmacy.checkoutMapModule
import com.pharmacy.product.productCardModule
import com.pharmacy.region.regionModule
import com.pharmacy.scanner.qrCodeScannerModule
import com.pharmacy.search.searchModule
import com.pharmacy.splash.splashModule
import com.pharmacy.user.address.addressModule
import com.pharmacy.user.profile.guest.guestProfileModule
import com.pharmacy.user.profile.profileModule
import com.pharmacy.user.userModule
import com.pharmacy.user.wishlist.wishModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object Modules {

    fun getListOfModules() = mutableListOf(
        managerModule,
        devToolsModule,
        mainModule,
        splashModule,
        onBoardingModule,
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
        wishModule,
        guestProfileModule,
        categoriesModule,
        ordersModule,
        addressModule,
        RESTModule
    )

    private val managerModule = module(true) {
        single { SPManager(androidApplication()) }
        single { RestManager(get(), get()) }
        single { WorkManager.getInstance(androidApplication()) }
        single { DBManager(androidApplication()) }
    }
}