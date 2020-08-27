package com.pharmacy.myapp.user

import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.user.repository.UserLocalDataSource
import com.pharmacy.myapp.user.repository.UserRepository
import org.koin.dsl.module


val userModule = module {
    single { UserLocalDataSource(get<DBManager>().customerDAO) }
    single { UserRepository(get()) }
}