package com.pulse.components.stub.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pulse.R

enum class StubType(@StringRes val toolbarTitle: Int, @DrawableRes val imageRes: Int, @StringRes val title: Int, @StringRes val description: Int) {

    UNDER_DEVELOPMENT(R.string.under_development, R.drawable.ic_stub, R.string.under_development_title, R.string.under_development_description),
    IATA_TRAVEL_PASS(R.string.iata_travel, R.drawable.ic_iata_travel_pass, R.string.iata_travel_title, R.string.under_development_description),
    INSURANCE_POLICY(R.string.insurance, R.drawable.ic_insurance_stub, R.string.insurance_title_empty, R.string.under_development_description),
    CHARITY(R.string.charityTitle, R.drawable.ic_charity_stub, R.string.charity_title_empty, R.string.under_development_description),
    ANOTHER_PAYMENT_TYPE(R.string.payment, R.drawable.ic_another_payment_type, R.string.naotherr_payment_type_title_empty, R.string.under_development_description),
}