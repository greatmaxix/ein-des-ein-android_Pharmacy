package com.pulse.components.onboarding.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.components.onboarding.model.Onboarding
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.item_onboarding.view.*

class OnboardingViewHolder(view: View, private val skip: (Onboarding.OnboardingType) -> Unit, private val next: (Onboarding.OnboardingType) -> Unit) :
    BaseViewHolder<Onboarding>(view) {

    override fun bind(item: Onboarding) = with(itemView) {
        ivPicture.setBackgroundResource(item.picture)
        mtvTitle.setText(item.title)
        mtvDescription.setText(item.description)
        mbNext.setText(item.next)
        mtvSkip.setText(item.skip)

        mbNext.setDebounceOnClickListener { next(item.type) }
        mtvSkip.setDebounceOnClickListener { skip(item.type) }
    }

    companion object {
        fun newInstance(parent: ViewGroup, skip: (Onboarding.OnboardingType) -> Unit, next: (Onboarding.OnboardingType) -> Unit) =
            OnboardingViewHolder(parent.inflate(R.layout.item_onboarding), skip, next)
    }
}