package com.pulse.user.wishlist

import com.pulse.data.local.DBManager
import com.pulse.user.wishlist.repository.WishLocalDataSource
import com.pulse.user.wishlist.repository.WishRemoteDataSource
import com.pulse.user.wishlist.repository.WishRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val wishModule = module {

    single { WishLocalDataSource(get<DBManager>().messageDAO) }
    single { WishRemoteDataSource(get()) }
    single { WishRepository(get(), get()) }

    viewModel { WishViewModel() }

    fragment { WishFragment(get()) }
}