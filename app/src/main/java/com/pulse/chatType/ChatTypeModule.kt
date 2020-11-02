package com.pulse.chatType

import com.pulse.chatType.repository.ChatTypeLocalDataSource
import com.pulse.chatType.repository.ChatTypeRemoteDataSource
import com.pulse.chatType.repository.ChatTypeRepository
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