package com.pulse

import androidx.work.WorkManager
import com.pulse.components.analyzes.category.analyzeCategoriesModule
import com.pulse.components.auth.authModule
import com.pulse.components.cart.cartModule
import com.pulse.components.categories.categoriesModule
import com.pulse.components.chat.chatModule
import com.pulse.components.chatType.chatTypeModule
import com.pulse.components.checkout.checkoutModule
import com.pulse.components.home.homeModule
import com.pulse.components.mercureService.mercureModule
import com.pulse.components.needHelp.needHelpModule
import com.pulse.components.onboarding.onboardingModule
import com.pulse.components.orders.details.orderModule
import com.pulse.components.orders.ordersModule
import com.pulse.components.payments.paymentsModule
import com.pulse.components.pharmacy.pharmacyModule
import com.pulse.components.product.productCardModule
import com.pulse.components.recipes.recipesModule
import com.pulse.components.region.regionModule
import com.pulse.components.scanner.qrCodeScannerModule
import com.pulse.components.search.searchModule
import com.pulse.components.splash.splashModule
import com.pulse.components.user.address.addressModule
import com.pulse.components.user.profile.guest.guestProfileModule
import com.pulse.components.user.profile.profileModule
import com.pulse.components.user.userModule
import com.pulse.components.user.wishlist.wishModule
import com.pulse.data.local.DBManager
import com.pulse.data.local.SPManager
import com.pulse.data.remote.RESTModule
import com.pulse.data.remote.RestManager
import com.pulse.main.mainModule
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

@FlowPreview
object Modules {

    private val managerModule = module(true) {
        single { SPManager(androidApplication()) }
        single { RestManager(get(), get()) }
        single { WorkManager.getInstance(androidApplication()) }
        single { DBManager(androidApplication()) }
    }

    val listOfModules = mutableListOf(
        managerModule,
        mainModule,
        splashModule,
        onboardingModule,
        profileModule,
        recipesModule,
        homeModule,
        authModule,
        qrCodeScannerModule,
        productCardModule,
        checkoutModule,
        orderModule,
        searchModule,
        cartModule,
        chatModule,
        chatTypeModule,
        pharmacyModule,
        regionModule,
        paymentsModule,
        userModule,
        wishModule,
        guestProfileModule,
        categoriesModule,
        ordersModule,
        addressModule,
        RESTModule,
        mercureModule,
        needHelpModule,
        analyzeCategoriesModule
    )
}