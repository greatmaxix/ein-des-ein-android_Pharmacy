package com.pulse.help

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.mockToast
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment: BaseMVVMFragment(R.layout.fragment_help) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        itemOrderHelp.mockToast()
        itemMoneybackHelp.mockToast()
        itemRecipesHelp.mockToast()
        itemPaymentHelp.mockToast()
        itemDeliveryHelp.mockToast()
    }

}