package com.pulse.data.mapper

import com.pulse.components.checkout.model.PaymentMethodAdapterModel
import com.pulse.components.payments.model.PaymentMethod

object PaymentMethodMapper {

    fun map() = PaymentMethod.values().map { PaymentMethodAdapterModel(it, it == PaymentMethod.CASH) }
}