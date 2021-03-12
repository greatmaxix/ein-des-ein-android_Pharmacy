package com.pulse.components.needHelp.contactUs

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.falseIfNull
import com.pulse.core.extensions.onClickDebounce
import com.pulse.core.extensions.toast
import com.pulse.databinding.FragmentContactUsBinding
import com.pulse.ui.text.hideError
import com.pulse.ui.text.showError
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ContactUsFragment(private val viewModel: ContactUsViewModel) : BaseMVVMFragment(R.layout.fragment_contact_us) {

    private val binding by viewBinding(FragmentContactUsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onBindLiveData() {
        observe(viewModel.resultLiveData) {
            requireContext().toast(R.string.your_request_accepted)
            navController.popBackStack()
        }
    }
}