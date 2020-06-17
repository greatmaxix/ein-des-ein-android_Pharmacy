package com.pharmacy.myapp

import com.pharmacy.myapp.auth.authModule
import org.koin.dsl.module

object Modules {

    fun getListOfModules() = mutableListOf(
        managerModule,
        authModule
    )

    private val managerModule = module(true) {
//        single { SPManager(androidApplication()) }
//        single { DBManager(androidApplication(), get()) }
//        single { RestManager(androidApplication(), get()) }
    }
}