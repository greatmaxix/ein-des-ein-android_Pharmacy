package com.pulse.components.payments.add

import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.MainGraphDirections.Companion.globalToStub
import com.pulse.R
import com.pulse.components.payments.PaymentsBaseFragment
import com.pulse.components.payments.add.AddPaymentMethodFragmentDirections.Companion.actionAddPaymentMethodToAddCard
import com.pulse.components.stub.model.StubType
import com.pulse.core.extensions.onClickDebounce
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.FragmentAddPaymentMethodBinding

class AddPaymentMethodFragment : PaymentsBaseFragment(R.layout.fragment_add_payment_method) {

    private val binding by viewBinding(FragmentAddPaymentMethodBinding::bind)

    override fun initUI() = with(binding) {
        mcvCard.setDebounceOnClickListener { navController.navigate(actionAddPaymentMethodToAddCard()) }
        mcvInsurance.onClickDebounce { navController.navigate(globalToStub(StubType.INSURANCE_POLICY)) }
        mcvOther.onClickDebounce { navController.navigate(globalToStub(StubType.ANOTHER_PAYMENT_TYPE)) }
    }
}