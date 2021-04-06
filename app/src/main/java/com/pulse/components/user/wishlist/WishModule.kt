package com.pulse.components.user.wishlist

import com.pulse.components.user.wishlist.repository.WishLocalDataSource
import com.pulse.components.user.wishlist.repository.WishRemoteDataSource
import com.pulse.components.user.wishlist.repository.WishRepository
import com.pulse.data.local.DBManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val wishModule = module {

    single { WishLocalDataSource(get<DBManager>().messageDAO) }
    single { WishRemoteDataSource(get()) }
    single { WishRepository(get(), get()) }

    viewModel { WishViewModel() }
}