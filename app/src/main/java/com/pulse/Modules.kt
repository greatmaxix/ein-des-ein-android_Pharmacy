package com.pulse

import androidx.work.WorkManager
import com.pulse.categories.categoriesModule
import com.pulse.chat.chatModule
import com.pulse.checkout.checkoutModule
import com.pulse.components.auth.authModule
import com.pulse.components.cart.cartModule
import com.pulse.components.onboarding.onboardingModule
import com.pulse.components.pharmacy.checkoutMapModule
import com.pulse.data.local.DBManager
import com.pulse.data.local.SPManager
import com.pulse.data.remote.RESTModule
import com.pulse.data.remote.RestManager
import com.pulse.devTools.devToolsModule
import com.pulse.home.homeModule
import com.pulse.main.mainModule
import com.pulse.orders.details.orderModule
import com.pulse.orders.ordersModule
import com.pulse.payments.paymentsModule
import com.pulse.product.productCardModule
import com.pulse.region.regionModule
import com.pulse.scanner.qrCodeScannerModule
import com.pulse.search.searchModule
import com.pulse.splash.splashModule
import com.pulse.user.address.addressModule
import com.pulse.user.profile.guest.guestProfileModule
import com.pulse.user.profile.profileModule
import com.pulse.user.userModule
import com.pulse.user.wishlist.wishModule
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