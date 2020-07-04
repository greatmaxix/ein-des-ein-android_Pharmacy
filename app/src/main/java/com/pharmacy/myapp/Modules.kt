package com.pharmacy.myapp

import com.pharmacy.myapp.auth.authModule
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.profile.profileModule
import com.pharmacy.myapp.splash.SplashModule.splashModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object Modules {

    fun getListOfModules() = mutableListOf(
        managerModule,
        splashModule,
        profileModule,
        authModule
    )

    private val managerModule = module(true) {
        single { SPManager(androidApplication()) }
        single { RestManager() }
//        single { DBManager(androidApplication(), get()) }
    }
}