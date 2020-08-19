package com.pharmacy.myapp.analyzes

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.toast
import kotlinx.android.synthetic.main.fragment_analyzes.*

class AnalyzesFragment: BaseMVVMFragment(R.layout.fragment_analyzes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        emptyContentAnalyzes.setButtonAction {
            requireContext().toast("TODO Записаться на анализы")
        }
    }

}