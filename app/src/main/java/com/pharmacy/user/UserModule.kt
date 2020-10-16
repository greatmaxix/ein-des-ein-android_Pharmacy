package com.pharmacy.user

import com.pharmacy.data.local.DBManager
import com.pharmacy.user.repository.CustomerLocalDataSource
import com.pharmacy.user.repository.CustomerRepository
import org.koin.dsl.module


val userModule = module {
    single { CustomerLocalDataSource(get<DBManager>().customerDAO) }
    single { CustomerRepository(get()) }
}