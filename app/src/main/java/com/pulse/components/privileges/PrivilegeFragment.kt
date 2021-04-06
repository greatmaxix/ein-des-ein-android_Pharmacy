package com.pulse.components.privileges

import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.databinding.FragmentPrivilegeBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class PrivilegeFragment : BaseToolbarFragment<PrivilegeViewModel>(R.layout.fragment_privilege, PrivilegeViewModel::class) {

    private val binding by viewBinding(FragmentPrivilegeBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()
    }

    override fun onClickNavigation() {
        navController.popBackStack(R.id.nav_profile, false)
    }
}