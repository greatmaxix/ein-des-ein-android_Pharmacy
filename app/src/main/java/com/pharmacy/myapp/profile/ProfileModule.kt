package com.pharmacy.myapp.profile

import com.pharmacy.myapp.profile.edit.EditProfileFragment
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {

    single { ProfileRepository(get(), get()) }

    viewModel { ProfileViewModel(get()) }

    fragment { ProfileFragment() }
    fragment { EditProfileFragment() }

}