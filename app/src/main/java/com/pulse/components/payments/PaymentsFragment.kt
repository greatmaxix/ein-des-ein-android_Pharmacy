package com.pulse.components.payments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textview.MaterialTextView
import com.pulse.R
import com.pulse.components.checkout.model.PaymentMethodModel
import com.pulse.components.payments.PaymentsFragmentDirections.Companion.actionPaymentsToAddPaymentMethodFragment
import com.pulse.components.payments.model.PaymentMethod
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.FragmentPaymentsBinding

class PaymentsFragment : PaymentsBaseFragment(R.layout.fragment_payments) {

    private val binding by viewBinding(FragmentPaymentsBinding::bind)
    private val paymentItemPadding by lazy { resources.getDimension(R.dimen._8sdp).toInt() }
    private val paymentItemTextColor by lazy { R.color.darkBlue.toColor }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        initPaymentMethods()
        mtvAddMethods.setDebounceOnClickListener {
            doNav(actionPaymentsToAddPaymentMethodFragment())
        }
    }

    private fun initPaymentMethods() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        PaymentMethod.values()
            .map { PaymentMethodModel(it, it == PaymentMethod.CASH) }
            .forEach { binding.llPaymentMethods.addView(createPaymentMethodItem(layoutParams, it)) }
    }

    private fun createPaymentMethodItem(
        params: ViewGroup.LayoutParams,
        it: PaymentMethodModel
    ): MaterialTextView {
        return MaterialTextView(requireContext()).apply {
            layoutParams = params
            setTextColor(paymentItemTextColor)
            setPadding(paymentItemPadding, paymentItemPadding, paymentItemPadding, paymentItemPadding)
            text = getString(it.method.title)
            setCompoundDrawablesWithIntrinsicBounds(0, 0, it.method.icon, 0)
        }
    }
}