package com.pulse.splash.adapter

import android.view.ViewGroup
import com.pulse.components.onboarding.model.Splash
import com.pulse.core.base.adapter.BaseRecyclerAdapter

class SplashPagerAdapter(items: List<Splash>, private val skip: () -> Unit, private val next: () -> Unit) : BaseRecyclerAdapter<Splash, SplashViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SplashViewHolder.newInstance(parent, skip, next)
}