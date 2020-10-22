package com.pulse.user

import com.pulse.data.local.DBManager
import com.pulse.user.repository.CustomerLocalDataSource
import com.pulse.user.repository.CustomerRepository
import org.koin.dsl.module


val userModule = module {
    single { CustomerLocalDataSource(get<DBManager>().customerDAO) }
    single { CustomerRepository(get()) }
}