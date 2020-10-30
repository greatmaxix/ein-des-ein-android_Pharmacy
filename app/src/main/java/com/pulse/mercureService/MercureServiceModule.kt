package com.pulse.mercureService

import com.here.oksse.OkSse
import com.pulse.data.local.DBManager
import com.pulse.mercureService.repository.MercureLocalDataSource
import com.pulse.mercureService.repository.MercureRepository
import okhttp3.OkHttpClient
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val mercureModule = module {

    single {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.SECONDS)
            .build()
        OkSse(client)
    }

    single { MercureLocalDataSource(get(), get<DBManager>().customerDAO, get<DBManager>().remoteKeysDAO, get<DBManager>().messageDAO, get<DBManager>().chatItemDAO) }
    single { MercureRepository(get()) }
}