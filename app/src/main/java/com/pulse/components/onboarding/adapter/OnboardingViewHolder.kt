package com.pulse.components.onboarding.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.onboarding.model.Onboarding
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.ItemOnboardingBinding

class OnboardingViewHolder(view: View, private val skip: (Onboarding.OnboardingType) -> Unit, private val next: (Onboarding.OnboardingType) -> Unit) :
    BaseViewHolder<Onboarding>(view) {

    private val binding by viewBinding(ItemOnboardingBinding::bind)

    override fun bind(item: Onboarding) = with(binding) {
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