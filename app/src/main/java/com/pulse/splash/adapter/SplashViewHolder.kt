package com.pulse.splash.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.components.onboarding.model.Splash
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.loadGlide
import com.pulse.core.extensions.onClick
import kotlinx.android.synthetic.main.item_splash.view.*

class SplashViewHolder(view: View, private val skip: () -> Unit, private val next: () -> Unit) : BaseViewHolder<Splash>(view) {

    override fun bind(item: Splash) = with(itemView) {
        ivBackground.loadGlide(item.bg)
        tvTitle.setText(item.title)
        tvSubTitle.setText(item.subTitle)

        mbNext.onClick(next)
        mtvSkip.onClick(skip)
    }

    companion object {
        fun newInstance(parent: ViewGroup, skip: () -> Unit, next: () -> Unit) = SplashViewHolder(parent.inflate(R.layout.item_splash), skip, next)
    }
}