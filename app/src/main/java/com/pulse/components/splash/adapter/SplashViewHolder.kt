package com.pulse.components.splash.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.splash.model.Splash
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.loadGlideDrawableByName
import com.pulse.core.extensions.onClickDebounce
import com.pulse.databinding.ItemSplashBinding

class SplashViewHolder(view: View, private val skip: () -> Unit, private val next: () -> Unit) : BaseViewHolder<Splash>(view) {

    private val binding by viewBinding(ItemSplashBinding::bind)

    override fun bind(item: Splash) = with(binding) {
        ivBackground.loadGlideDrawableByName(item.bg)
        mtvTitle.setText(item.title)
        mtvSubtitle.setText(item.subTitle)
        mbNext.onClickDebounce(next)
        mtvSkip.onClickDebounce(skip)
    }

    companion object {

        fun newInstance(parent: ViewGroup, skip: () -> Unit, next: () -> Unit) = SplashViewHolder(parent.inflate(R.layout.item_splash), skip, next)
    }
}