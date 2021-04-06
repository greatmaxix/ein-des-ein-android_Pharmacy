package com.pulse.components.privileges

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.databinding.FragmentPrivilegeBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class PrivilegeFragment(private val viewModel: PrivilegeViewModel) : BaseMVVMFragment(R.layout.fragment_privilege) {

    private val binding by viewBinding(FragmentPrivilegeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton() {
            navController.popBackStack(R.id.nav_profile, false)
        }
    }
}