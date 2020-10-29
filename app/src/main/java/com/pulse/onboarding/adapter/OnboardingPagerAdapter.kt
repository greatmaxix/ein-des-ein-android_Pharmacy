package com.pulse.onboarding.adapter

import android.view.ViewGroup
import com.pulse.core.base.adapter.BaseRecyclerAdapter
import com.pulse.onboarding.model.Onboarding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class OnboardingPagerAdapter(items: List<Onboarding>, private val skip: (Onboarding.OnboardingType) -> Unit, private val next: (Onboarding.OnboardingType) -> Unit) :
    BaseRecyclerAdapter<Onboarding, OnboardingViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OnboardingViewHolder.newInstance(parent, skip, next)

}