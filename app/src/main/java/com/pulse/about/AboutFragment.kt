package com.pulse.about

import android.os.Bundle
import android.view.View
import com.pulse.BuildConfig
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.mockToast
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : BaseMVVMFragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        tvVersionAbout.text = getString(R.string.versionAbout, BuildConfig.VERSION_NAME)

        itemUserAgreement.mockToast("Пользовательское соглашение")
        itemPersonalData.mockToast("О Персональных данных")
        itemDataUsage.mockToast("Условия использования данных")
        itemCashback.mockToast("Про кешбек")
    }
}