package com.pulse.components.chat

import com.pulse.components.chat.dialog.ChatReviewBottomSheetDialogFragment
import com.pulse.components.chat.dialog.SendBottomSheetDialogFragment
import com.pulse.components.chat.model.chat.ChatItem
import com.pulse.components.chat.repository.ChatLocalDataSource
import com.pulse.components.chat.repository.ChatRemoteDataSource
import com.pulse.components.chat.repository.ChatRepository
import com.pulse.data.local.DBManager
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@FlowPreview
@OptIn(KoinApiExtension::class)
val chatModule = module {

    single { ChatLocalDataSource(get(), get<DBManager>().customerDAO, get<DBManager>().remoteKeysDAO, get<DBManager>().messageDAO, get<DBManager>().chatItemDAO) }
    single { ChatRemoteDataSource(get()) }
    single { ChatRepository(get(), get()) }

    viewModel { (chat: ChatItem) -> ChatViewModel(androidApplication(), get(), chat, get(), get()) }

    fragment { ChatFragment() }

    fragment { ChatReviewBottomSheetDialogFragment() }

    fragment { SendBottomSheetDialogFragment() }
}