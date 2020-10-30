package com.pulse.splash.adapter

import android.view.ViewGroup
import com.pulse.components.onboarding.model.Onboarding
import com.pulse.core.base.adapter.BaseRecyclerAdapter

class OnboardingPagerAdapter(items: List<Onboarding>, private val skip: () -> Unit, private val next: () -> Unit) : BaseRecyclerAdapter<Onboarding, OnboardingViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OnboardingViewHolder.newInstance(parent, skip, next)
}