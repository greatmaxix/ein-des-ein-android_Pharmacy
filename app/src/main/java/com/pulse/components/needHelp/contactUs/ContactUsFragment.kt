package com.pulse.components.needHelp.contactUs

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.onClickDebounce
import com.pulse.core.extensions.toast
import com.pulse.databinding.FragmentContactUsBinding
import com.pulse.ui.text.hideError
import com.pulse.ui.text.showError
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ContactUsFragment : BaseToolbarFragment<ContactUsViewModel>(R.layout.fragment_contact_us, ContactUsViewModel::class) {

    private val binding by viewBinding(FragmentContactUsBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()

        tilNote.editText?.doAfterTextChanged {
            if (it?.isNotBlank().falseIfNull()) {
                tilNote.hideError()
            }
        }
        mbSendRequest.onClickDebounce {
            val text = tilNote.editText?.text?.toString().orEmpty()
            if (text.isBlank()) {
                tilNote.showError(getString(R.string.note_cant_be_empty))
            } else {
                viewModel.sendRequest(text)
            }
        }
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(viewModel.resultEvent.events) {
            requireContext().toast(R.string.your_request_accepted)
            navController.popBackStack()
        }
    }
}