package com.pulse.splash.adapter

import android.view.ViewGroup
import com.pulse.core.base.adapter.BaseRecyclerAdapter
import com.pulse.splash.model.Splash

class SplashPagerAdapter(items: List<Splash>, private val skip: () -> Unit, private val next: () -> Unit) : BaseRecyclerAdapter<Splash, SplashViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SplashViewHolder.newInstance(parent, skip, next)
}