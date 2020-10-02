package com.pharmacy.myapp.about

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.toast
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : BaseMVVMFragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        tvVersionAbout.text = getString(R.string.versionAbout, BuildConfig.VERSION_NAME)

        itemUserAgreement.setOnClick { requireContext().toast("Пользовательское соглашение") }
        itemPersonalData.setOnClick { requireContext().toast("О Персональных данных") }
        itemDataUsage.setOnClick { requireContext().toast("Условия использования данных") }
        itemCashback.setOnClick { requireContext().toast("Про кешбек") }
    }
}