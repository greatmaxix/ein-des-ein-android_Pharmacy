package com.pharmacy.analyzes

import android.os.Bundle
import android.view.View
import com.pharmacy.R
import com.pharmacy.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.core.extensions.toast
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