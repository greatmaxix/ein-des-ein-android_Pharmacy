package com.pulse.payments

import android.os.Bundle
import android.view.View
import com.pulse.R
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.toast
import com.pulse.payments.AddPaymentMethodFragmentDirections.Companion.actionAddPaymentMethodToAddCard
import kotlinx.android.synthetic.main.fragment_add_payment_method.*

class AddPaymentMethodFragment : PaymentsBaseFragment(R.layout.fragment_add_payment_method) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardCardAddPayment.setDebounceOnClickListener {
            doNav(actionAddPaymentMethodToAddCard())
        }

        cardOtherAddPayment.setDebounceOnClickListener {
            requireContext().toast("TODO add other payment")
        }
    }
}