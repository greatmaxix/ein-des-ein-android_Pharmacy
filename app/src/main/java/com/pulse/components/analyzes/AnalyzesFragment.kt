package com.pulse.components.analyzes

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.toast
import com.pulse.databinding.FragmentAnalyzesBinding

class AnalyzesFragment : BaseMVVMFragment(R.layout.fragment_analyzes) {

    private val binding by viewBinding(FragmentAnalyzesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        viewEmptyContent.setButtonAction {
            requireContext().toast("TODO Записаться на анализы")
        }
    }
}