package com.pharmacy.myapp.chatType

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToHome
import com.pharmacy.myapp.R
import com.pharmacy.myapp.chatType.ChatTypeFragmentDirections.Companion.fromChatTypeToChat
import com.pharmacy.myapp.chatType.model.ChatType
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import com.pharmacy.myapp.data.remote.model.chat.ChatItem
import kotlinx.android.synthetic.main.fragment_chat_type.*

class ChatTypeFragment(private val viewModel: ChatTypeViewModel) : BaseMVVMFragment(R.layout.fragment_chat_type) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton {
            doNav(globalToHome())
        }
        attachBackPressCallback {
            doNav(globalToHome())
        }
        cardPharmacistChatType.setDebounceOnClickListener { viewModel.setChatType(ChatType.PHARMACIST) }
        cardDoctorChatType.setDebounceOnClickListener { viewModel.setChatType(ChatType.DOCTOR) }
        btnNextChatType.isEnabled = false
        btnNextChatType.setDebounceOnClickListener {
            observeResult<ChatItem> {
                liveData = viewModel.createChat()
                onEmmit = { doNav(fromChatTypeToChat(this)) }
            }
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()

        observeNullable(viewModel.chatTypeLiveData) {
            cardPharmacistChatType.isSelected = it == ChatType.PHARMACIST
            cardDoctorChatType.isSelected = it == ChatType.DOCTOR
            btnNextChatType.isEnabled = it != null
        }
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
    }
}