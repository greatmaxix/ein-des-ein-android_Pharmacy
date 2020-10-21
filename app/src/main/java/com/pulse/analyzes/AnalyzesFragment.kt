package com.pulse.analyzes

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.toast
import kotlinx.android.synthetic.main.fragment_analyzes.*

class AnalyzesFragment : BaseMVVMFragment(R.layout.fragment_analyzes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        emptyContentAnalyzes.setButtonAction {
            requireContext().toast("TODO Записаться на анализы")
        }
    }

}