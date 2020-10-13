package com.pharmacy.chat

import com.pharmacy.chat.dialog.ChatReviewBottomSheetDialogFragment
import com.pharmacy.chat.dialog.SendBottomSheetDialogFragment
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {

    single { ChatRepository(get(), get()) }

    viewModel { ChatViewModel(androidApplication(), get()) }

    fragment { ChatFragment(get()) }

    fragment { ChatReviewBottomSheetDialogFragment() }

    fragment { SendBottomSheetDialogFragment() }
}