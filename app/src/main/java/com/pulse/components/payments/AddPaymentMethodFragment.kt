package com.pulse.components.payments

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.payments.AddPaymentMethodFragmentDirections.Companion.actionAddPaymentMethodToAddCard
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.toast
import com.pulse.databinding.FragmentAddPaymentMethodBinding

class AddPaymentMethodFragment : PaymentsBaseFragment(R.layout.fragment_add_payment_method) {

    private val binding by viewBinding(FragmentAddPaymentMethodBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        mcvCard.setDebounceOnClickListener { doNav(actionAddPaymentMethodToAddCard()) }
        mcvOther.setDebounceOnClickListener { requireContext().toast("TODO add other payment") }
    }
}