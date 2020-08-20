package com.pharmacy.myapp.quickAccess

import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.quickAccess.repository.QuickAccessLocalDataSource
import com.pharmacy.myapp.quickAccess.repository.QuickAccessRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val quickAccessModule = module {
    single { QuickAccessLocalDataSource(get<DBManager>().quickAccessDAO()) }
    single { QuickAccessRepository(get()) }

    viewModel { QuickAccessViewModel(get()) }

    fragment { QuickAccessFragment(get(), get()) }
}