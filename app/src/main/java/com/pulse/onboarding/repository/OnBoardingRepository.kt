package com.pulse.onboarding.repository

import com.pulse.data.local.SPManager

class OnBoardingRepository(private val sp: SPManager) {

    fun setOnBoardingShown() {
        sp.isNeedOnBoarding = true
    }

}