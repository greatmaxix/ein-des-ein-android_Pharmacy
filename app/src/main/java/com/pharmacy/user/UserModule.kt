package com.pharmacy.user

import com.pharmacy.data.local.DBManager
import com.pharmacy.user.repository.UserLocalDataSource
import com.pharmacy.user.repository.UserRepository
import org.koin.dsl.module


val userModule = module {
    single { UserLocalDataSource(get<DBManager>().customerDAO) }
    single { UserRepository(get()) }
}