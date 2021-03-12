package com.pulse.components.checkout.model

import android.os.Parcelable
import com.pulse.components.payments.model.PaymentMethod
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentMethodAdapterModel(
    val method: PaymentMethod,
    val isChecked: Boolean = false
) : Parcelable