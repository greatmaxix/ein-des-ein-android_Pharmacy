package com.pulse.components.about

import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.BuildConfig.VERSION_NAME
import com.pulse.MainGraphDirections.Companion.globalToStub
import com.pulse.R
import com.pulse.components.stub.model.StubType
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.onClickDebounce
import com.pulse.databinding.FragmentAboutBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AboutFragment : BaseToolbarFragment<AboutViewModel>(R.layout.fragment_about, AboutViewModel::class) {

    private val binding by viewBinding(FragmentAboutBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()

        mtvVersion.text = getString(R.string.versionAbout, VERSION_NAME)

        itemUserAgreement.onClickDebounce { navController.navigate(globalToStub(StubType.UNDER_DEVELOPMENT)) }
        itemPersonalData.onClickDebounce { navController.navigate(globalToStub(StubType.UNDER_DEVELOPMENT)) }
        itemDataUsage.onClickDebounce { navController.navigate(globalToStub(StubType.UNDER_DEVELOPMENT)) }
        itemCashback.onClickDebounce { navController.navigate(globalToStub(StubType.UNDER_DEVELOPMENT)) }
    }
}