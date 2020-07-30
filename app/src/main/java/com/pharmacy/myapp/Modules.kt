package com.pharmacy.myapp

import androidx.work.WorkManager
import com.pharmacy.myapp.auth.authModule
import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.home.homeModule
import com.pharmacy.myapp.profile.profileModule
import com.pharmacy.myapp.qrCodeScanner.qrCodeScannerModule
import com.pharmacy.myapp.splash.splashModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object Modules {

    fun getListOfModules() = mutableListOf(
        managerModule,
        splashModule,
        profileModule,
        homeModule,
        authModule,
        qrCodeScannerModule
    )

    private val managerModule = module(true) {
        single { SPManager(androidApplication()) }
        single { RestManager() }
        single { WorkManager.getInstance(androidApplication()) }
        single { DBManager(androidApplication()) }
    }
}