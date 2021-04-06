package com.pulse.components.chatType

import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.MainGraphDirections.Companion.globalToHome
import com.pulse.R
import com.pulse.components.chatType.ChatTypeFragmentDirections.Companion.fromChatTypeToChat
import com.pulse.components.chatType.model.ChatType
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.FragmentChatTypeBinding

class ChatTypeFragment : BaseToolbarFragment<ChatTypeViewModel>(R.layout.fragment_chat_type, ChatTypeViewModel::class) {

    private val binding by viewBinding(FragmentChatTypeBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()
        attachBackPressCallback { navController.navigate(globalToHome()) }

        mcvPharmacist.setDebounceOnClickListener { viewModel.setChatType(ChatType.PHARMACIST) }
        mcvDoctor.setDebounceOnClickListener { viewModel.setChatType(ChatType.DOCTOR) }
        mbNext.isEnabled = false
        mbNext.setDebounceOnClickListener { viewModel.createChat() }
    }

    override fun onClickNavigation() {
        navController.navigate(globalToHome())
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.newChatEvent.events) {
            navController.navigate(fromChatTypeToChat(it))
        }
    }

    override fun onBindStates()  = with(lifecycleScope) {
        observe(viewModel.chatTypeState) {
            binding.mcvPharmacist.isSelected = it == ChatType.PHARMACIST
            binding.mcvDoctor.isSelected = it == ChatType.DOCTOR
            binding.mbNext.isEnabled = it != null
        }
    }
}