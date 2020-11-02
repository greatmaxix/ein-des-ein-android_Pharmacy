package com.pulse.chatType

import android.os.Bundle
import android.view.View
import com.pulse.MainGraphDirections.Companion.globalToHome
import com.pulse.R
import com.pulse.chatType.ChatTypeFragmentDirections.Companion.fromChatTypeToChat
import com.pulse.chatType.model.ChatType
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.setDebounceOnClickListener
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
            observeResult(viewModel.createChat()) {
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