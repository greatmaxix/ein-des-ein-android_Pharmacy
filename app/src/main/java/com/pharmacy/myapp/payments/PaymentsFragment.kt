package com.pharmacy.myapp.payments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textview.MaterialTextView
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.model.TempPaymentMethod
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import com.pharmacy.myapp.data.remote.DummyData
import com.pharmacy.myapp.payments.PaymentsFragmentDirections.Companion.actionPaymentsToAddPaymentMethodFragment
import kotlinx.android.synthetic.main.fragment_payments.*

class PaymentsFragment : PaymentsBaseFragment(R.layout.fragment_payments) {

    private val paymentItemPadding by lazy { resources.getDimension(R.dimen._8sdp).toInt() }
    private val paymentItemTextColor by lazy { R.color.darkBlue.toColor }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPaymentMethods()
        tvAddMethodsPayments.setDebounceOnClickListener {
            doNav(actionPaymentsToAddPaymentMethodFragment())
        }
    }

    private fun initPaymentMethods() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        DummyData.paymentMethod.forEach {
            llPaymentMethodsPayments.addView(createPaymentMethodItem(layoutParams, it))
        }
    }

    private fun createPaymentMethodItem(
        params: ViewGroup.LayoutParams,
        it: TempPaymentMethod
    ): MaterialTextView {
        return MaterialTextView(requireContext()).apply {
            layoutParams = params
            setTextColor(paymentItemTextColor)
            setPadding(paymentItemPadding, paymentItemPadding, paymentItemPadding, paymentItemPadding)
            text = it.name
            setCompoundDrawablesWithIntrinsicBounds(0, 0, it.icon, 0)
        }
    }
}