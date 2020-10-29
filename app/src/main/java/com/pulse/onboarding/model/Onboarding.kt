package com.pulse.onboarding.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Onboarding(
    @DrawableRes val picture: Int,
    @StringRes val title: Int,
    @StringRes val description: Int,
    @StringRes val next: Int,
    @StringRes val skip: Int,
    val type: OnboardingType
) {

    enum class OnboardingType {
        REGION, AUTH;
    }

}