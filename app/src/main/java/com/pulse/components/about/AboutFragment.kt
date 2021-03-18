package com.pulse.components.about

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.BuildConfig.VERSION_NAME
import com.pulse.MainGraphDirections.Companion.globalToStub
import com.pulse.R
import com.pulse.components.stub.model.StubType
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.onClickDebounce
import com.pulse.databinding.FragmentAboutBinding

class AboutFragment : BaseMVVMFragment(R.layout.fragment_about) {

    private val binding by viewBinding(FragmentAboutBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        mtvVersion.text = getString(R.string.versionAbout, VERSION_NAME)

        itemUserAgreement.onClickDebounce { navController.navigate(globalToStub(StubType.UNDER_DEVELOPMENT)) }
        itemPersonalData.onClickDebounce { navController.navigate(globalToStub(StubType.UNDER_DEVELOPMENT)) }
        itemDataUsage.onClickDebounce { navController.navigate(globalToStub(StubType.UNDER_DEVELOPMENT)) }
        itemCashback.onClickDebounce { navController.navigate(globalToStub(StubType.UNDER_DEVELOPMENT)) }
    }
}