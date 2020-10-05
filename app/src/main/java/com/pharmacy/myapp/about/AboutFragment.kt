package com.pharmacy.myapp.about

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.mockToast
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