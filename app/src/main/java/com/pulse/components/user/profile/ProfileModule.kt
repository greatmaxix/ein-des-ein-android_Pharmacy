package com.pulse.components.user.profile

import com.pulse.components.user.profile.edit.ChangePhotoBottomSheetDialogFragment
import com.pulse.components.user.profile.repository.ProfileRepository
import com.pulse.data.local.DBManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {

    single { ProfileRepository(get(), get(), get<DBManager>().customerDAO, get<DBManager>().recentlyViewedDAO, get<DBManager>().addressDAO) }

    viewModel { ProfileViewModel(androidApplication(), get()) }

    fragment { ChangePhotoBottomSheetDialogFragment() }
}