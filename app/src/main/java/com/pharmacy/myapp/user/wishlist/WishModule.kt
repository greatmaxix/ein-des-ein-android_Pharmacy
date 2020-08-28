package com.pharmacy.myapp.user.wishlist

import com.pharmacy.myapp.user.wishlist.repository.WishRemoteDataSource
import com.pharmacy.myapp.user.wishlist.repository.WishRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val wishModule = module {

    single { WishRemoteDataSource(get()) }
    single { WishRepository(get()) }

    viewModel { WishViewModel() }

    fragment { WishFragment(get()) }
}