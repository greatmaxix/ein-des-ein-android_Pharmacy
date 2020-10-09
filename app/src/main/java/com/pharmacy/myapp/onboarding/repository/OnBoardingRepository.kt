package com.pharmacy.myapp.onboarding.repository

import com.pharmacy.myapp.data.local.SPManager

class OnBoardingRepository(private val sp: SPManager) {

    fun setOnBoardingShown() {
        sp.isNeedOnBoarding = true
    }

}