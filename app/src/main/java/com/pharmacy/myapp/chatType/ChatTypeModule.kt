package com.pharmacy.myapp.chatType

import com.pharmacy.myapp.chatType.repository.ChatTypeLocalDataSource
import com.pharmacy.myapp.chatType.repository.ChatTypeRemoteDataSource
import com.pharmacy.myapp.chatType.repository.ChatTypeRepository
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatTypeModule = module {

    single { ChatTypeLocalDataSource(get()) }
    single { ChatTypeRemoteDataSource(get()) }
    single { ChatTypeRepository(get(), get()) }

    viewModel { ChatTypeViewModel(get()) }

    fragment { ChatTypeFragment(get()) }
}