package com.pharmacy.myapp.onboarding

import com.pharmacy.myapp.data.local.SPManager

class OnboardingRepository(private val sp: SPManager) {

    fun setOnboardingShown() {
        sp.isOnboardingShown = true
    }

}