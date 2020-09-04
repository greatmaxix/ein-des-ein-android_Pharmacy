package com.pharmacy.myapp.user.profile.guest

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val guestProfileModule = module {

    viewModel { GuestProfileViewModel(get()) }

    fragment { GuestProfileFragment(get()) }

}