package com.pharmacy.myapp.chat

import com.pharmacy.myapp.chat.dialog.ChatReviewBottomSheetDialogFragment
import com.pharmacy.myapp.chat.dialog.SendBottomSheetDialogFragment
import com.pharmacy.myapp.chat.repository.ChatLocalDataSource
import com.pharmacy.myapp.chat.repository.ChatRemoteDataSource
import com.pharmacy.myapp.chat.repository.ChatRepository
import com.pharmacy.myapp.data.local.DBManager
import com.pharmacy.myapp.data.remote.model.chat.ChatItem
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