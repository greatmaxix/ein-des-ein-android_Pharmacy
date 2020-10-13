package com.pharmacy.onboarding.repository

import com.pharmacy.data.local.SPManager

class OnBoardingRepository(private val sp: SPManager) {

    fun setOnBoardingShown() {
        sp.isNeedOnBoarding = true
    }

}