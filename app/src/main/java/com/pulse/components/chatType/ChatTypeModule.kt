package com.pulse.components.chatType

import com.pulse.components.chatType.repository.ChatTypeLocalDataSource
import com.pulse.components.chatType.repository.ChatTypeRemoteDataSource
import com.pulse.components.chatType.repository.ChatTypeRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatTypeModule = module {

    single { ChatTypeLocalDataSource(get()) }
    single { ChatTypeRemoteDataSource(get()) }
    single { ChatTypeRepository(get(), get()) }

    viewModel { ChatTypeViewModel(get()) }
}