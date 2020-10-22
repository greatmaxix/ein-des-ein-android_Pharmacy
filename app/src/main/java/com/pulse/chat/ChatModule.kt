package com.pulse.chat

import com.pulse.chat.dialog.ChatReviewBottomSheetDialogFragment
import com.pulse.chat.dialog.SendBottomSheetDialogFragment
import com.pulse.chat.repository.ChatLocalDataSource
import com.pulse.chat.repository.ChatRemoteDataSource
import com.pulse.chat.repository.ChatRepository
import com.pulse.data.local.DBManager
import com.pulse.data.remote.model.chat.ChatItem
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@FlowPreview
@OptIn(KoinApiExtension::class)
val chatModule = module {

    single { ChatLocalDataSource(get(), get<DBManager>().customerDAO, get<DBManager>().remoteKeysDAO, get<DBManager>().messageDAO) }
    single { ChatRemoteDataSource(get()) }
    single { ChatRepository(get(), get()) }

    viewModel { (chat: ChatItem) -> ChatViewModel(androidApplication(), get(), chat) }

    fragment { ChatFragment() }

    fragment { ChatReviewBottomSheetDialogFragment() }

    fragment { SendBottomSheetDialogFragment() }
}