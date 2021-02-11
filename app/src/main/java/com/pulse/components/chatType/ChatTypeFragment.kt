package com.pulse.components.chatType

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.MainGraphDirections.Companion.globalToHome
import com.pulse.R
import com.pulse.components.chatType.ChatTypeFragmentDirections.Companion.fromChatTypeToChat
import com.pulse.components.chatType.model.ChatType
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.FragmentChatTypeBinding

class ChatTypeFragment(private val viewModel: ChatTypeViewModel) : BaseMVVMFragment(R.layout.fragment_chat_type) {

    private val binding by viewBinding(FragmentChatTypeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton { doNav(globalToHome()) }
        attachBackPressCallback { doNav(globalToHome()) }

        mcvPharmacist.setDebounceOnClickListener { viewModel.setChatType(ChatType.PHARMACIST) }
        mcvDoctor.setDebounceOnClickListener { viewModel.setChatType(ChatType.DOCTOR) }
        mbNext.isEnabled = false
        mbNext.setDebounceOnClickListener {
            observeResult(viewModel.createChat()) {
                onEmmit = { doNav(fromChatTypeToChat(this)) }
            }
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()

        observeNullable(viewModel.chatTypeLiveData) {
            binding.mcvPharmacist.isSelected = it == ChatType.PHARMACIST
            binding.mcvDoctor.isSelected = it == ChatType.DOCTOR
            binding.mbNext.isEnabled = it != null
        }
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
    }
}