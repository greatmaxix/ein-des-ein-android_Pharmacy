package com.pulse.user

import com.pulse.data.local.DBManager
import com.pulse.user.repository.CustomerLocalDataSource
import com.pulse.user.repository.CustomerRemoteDataSource
import com.pulse.user.repository.CustomerRepository
import com.pulse.user.repository.CustomerUseCase
import org.koin.dsl.module

val userModule = module {
    single { CustomerRemoteDataSource(get()) }
    single { CustomerLocalDataSource(get<DBManager>().customerDAO) }
    single { CustomerRepository(get(), get()) }
    single { CustomerUseCase(get(), get()) }
}