package com.pulse.splash.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.components.onboarding.model.Onboarding
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.loadGlide
import com.pulse.core.extensions.onClick
import kotlinx.android.synthetic.main.item_onboarding.view.*

class OnboardingViewHolder(view: View, private val skip: () -> Unit, private val next: () -> Unit) : BaseViewHolder<Onboarding>(view) {

    override fun bind(item: Onboarding) = with(itemView) {
        ivBackground.loadGlide(item.bg)
        tvTitle.setText(item.title)
        tvSubTitle.setText(item.subTitle)

        mbNext.onClick(next)
        tvSkip.onClick(skip)
    }

    companion object {
        fun newInstance(parent: ViewGroup, skip: () -> Unit, next: () -> Unit) = OnboardingViewHolder(parent.inflate(R.layout.item_onboarding), skip, next)
    }
}