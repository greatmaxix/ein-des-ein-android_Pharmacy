package com.pulse.components.payments.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pulse.R

enum class PaymentMethod(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val isActive: Boolean
) {

    GOOGLE_PAY(R.string.google_pay_payment, R.drawable.ic_google_pay, false),
    KASPI_BANK(R.string.kaspi_bank, R.drawable.ic_kaspi_bank, false),
    INSURANCE(R.string.insurance, R.drawable.ic_shield, false),
    CASH(R.string.cash, R.drawable.ic_cash, true)
}