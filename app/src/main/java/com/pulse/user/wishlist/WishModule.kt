package com.pulse.user.wishlist

import com.pulse.user.wishlist.repository.WishRemoteDataSource
import com.pulse.user.wishlist.repository.WishRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
val wishModule = module {

    single { WishRemoteDataSource(get()) }
    single { WishRepository(get()) }

    viewModel { WishViewModel() }

    fragment { WishFragment(get()) }
}