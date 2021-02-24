package com.pulse.components.privileges

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.radiobutton.MaterialRadioButton
import com.pulse.R
import com.pulse.components.privileges.PrivilegeTypeFragmentDirections.Companion.fromPrivilegeTypeToPrivilege
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.getColorStateList
import com.pulse.core.extensions.getDimensionPixelSize
import com.pulse.core.extensions.onClickDebounce
import com.pulse.data.remote.DummyData
import com.pulse.databinding.FragmentPrivilegeTypeBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class PrivilegeTypeFragment : BaseMVVMFragment(R.layout.fragment_privilege_type) {

    private val binding by viewBinding(FragmentPrivilegeTypeBinding::bind)
    private val radioButtonPadding by lazy { getDimensionPixelSize(R.dimen._8sdp) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        initPrivilegeType()

        mbNext.onClickDebounce {
            navController.navigate(fromPrivilegeTypeToPrivilege())
        }
    }

    private fun initPrivilegeType() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        DummyData.privilegeType.forEach {
            binding.rgPrivilegeType.addView(createRadioButton(layoutParams, it))
        }
    }

    private fun createRadioButton(layoutParams: ViewGroup.LayoutParams, name: String) = MaterialRadioButton(requireContext()).apply {
        this.layoutParams = layoutParams
        setPadding(radioButtonPadding, (radioButtonPadding * 1.5).toInt(), radioButtonPadding, (radioButtonPadding * 1.5).toInt())
        text = name
        setTextColor(getColorStateList(R.color.selector_text_payment))
        buttonTintList = getColorStateList(R.color.selector_tint_button_payment)
    }
}