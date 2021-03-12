package com.pulse

import androidx.work.WorkManager
import com.pulse.components.analyzes.category.analyzeCategoriesModule
import com.pulse.components.analyzes.checkout.analyzeCheckoutModule
import com.pulse.components.analyzes.clinic.card.clinicCardModule
import com.pulse.components.analyzes.clinic.tabs.clinicTabsModule
import com.pulse.components.analyzes.details.analyzeDetailsModule
import com.pulse.components.analyzes.list.analyzesModule
import com.pulse.components.analyzes.order.analyzeOrderModule
import com.pulse.components.auth.authModule
import com.pulse.components.cart.cartModule
import com.pulse.components.categories.categoriesModule
import com.pulse.components.chat.chatModule
import com.pulse.components.chatType.chatTypeModule
import com.pulse.components.checkout.checkoutModule
import com.pulse.components.home.homeModule
import com.pulse.components.language.languageModule
import com.pulse.components.mercureService.mercureModule
import com.pulse.components.needHelp.contactUs.contactUsModule
import com.pulse.components.needHelp.needHelpModule
import com.pulse.components.onboarding.onboardingModule
import com.pulse.components.orders.details.orderModule
import com.pulse.components.orders.ordersModule
import com.pulse.components.payments.paymentsModule
import com.pulse.components.pharmacy.pharmacyModule
import com.pulse.components.privileges.privilegeModule
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
import com.pulse.core.extensions.dataStore
import com.pulse.core.locale.ILocaleManager
import com.pulse.core.locale.LocaleManager
import com.pulse.data.local.DBManager
import com.pulse.data.remote.RESTModule
import com.pulse.data.remote.RestManager
import com.pulse.main.mainModule
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@FlowPreview
object Modules {

    private val managerModule = module(true) {
        single { androidContext().dataStore }
        single { RestManager(get(), get()) }
        single { WorkManager.getInstance(androidApplication()) }
        single { DBManager(androidApplication()) }
        single<ILocaleManager> { LocaleManager.getInstance(get()) }
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
        analyzesModule,
        analyzeCategoriesModule,
        analyzeDetailsModule,
        analyzeCheckoutModule,
        clinicCardModule,
        clinicTabsModule,
        analyzeOrderModule,
        privilegeModule,
        languageModule,
        contactUsModule
    )
}