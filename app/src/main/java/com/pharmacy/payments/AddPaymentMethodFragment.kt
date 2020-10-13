package com.pharmacy.payments

import android.os.Bundle
import android.view.View
import com.pharmacy.R
import com.pharmacy.core.extensions.setDebounceOnClickListener
import com.pharmacy.core.extensions.toast
import com.pharmacy.payments.AddPaymentMethodFragmentDirections.Companion.actionAddPaymentMethodToAddCard
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