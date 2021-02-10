package com.pulse.components.user

import com.pulse.components.user.repository.CustomerLocalDataSource
import com.pulse.components.user.repository.CustomerRemoteDataSource
import com.pulse.components.user.repository.CustomerRepository
import com.pulse.components.user.repository.CustomerUseCase
import com.pulse.data.local.DBManager
import org.koin.dsl.module

val userModule = module {
    single { CustomerRemoteDataSource(get()) }
    single { CustomerLocalDataSource(get<DBManager>().customerDAO) }
    single { CustomerRepository(get(), get()) }
    single { CustomerUseCase(get(), get()) }
}