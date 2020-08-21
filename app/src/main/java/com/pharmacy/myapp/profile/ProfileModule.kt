package com.pharmacy.myapp.profile

import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.profile.edit.ChangePhotoBottomSheetDialogFragment
import com.pharmacy.myapp.profile.edit.EditProfileFragment
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {

    single { ProfileRepository(get(), get(), get<DBManager>().customerDAO()) }

    viewModel { ProfileViewModel(androidApplication(), get()) }

    fragment { ProfileFragment() }
    fragment { EditProfileFragment() }
    fragment { ChangePhotoBottomSheetDialogFragment() }

}