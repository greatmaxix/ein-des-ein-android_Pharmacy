package com.pharmacy.myapp.payments

import android.os.Bundle
import android.view.View
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import com.pharmacy.myapp.core.extensions.toast
import com.pharmacy.myapp.payments.AddPaymentMethodFragmentDirections.Companion.actionAddPaymentMethodToAddCard
import kotlinx.android.synthetic.main.fragment_add_payment_method.*

class AddPaymentMethodFragment : PaymentsBaseFragment(R.layout.fragment_add_payment_method) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton(R.drawable.ic_arrow_back)

        cardCardAddPayment.setDebounceOnClickListener {
            doNav(actionAddPaymentMethodToAddCard())
        }

        cardOtherAddPayment.setDebounceOnClickListener {
            requireContext().toast("TODO add other payment")
        }
    }
}