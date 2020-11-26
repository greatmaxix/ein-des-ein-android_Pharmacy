package com.pulse.splash.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.loadGlideDrawableByName
import com.pulse.core.extensions.onClickDebounce
import com.pulse.splash.model.Splash
import kotlinx.android.synthetic.main.item_splash.view.*

class SplashViewHolder(view: View, private val skip: () -> Unit, private val next: () -> Unit) : BaseViewHolder<Splash>(view) {

    override fun bind(item: Splash) = with(itemView) {
        ivBackground.loadGlideDrawableByName(item.bg)
        tvTitle.setText(item.title)
        tvSubTitle.setText(item.subTitle)

        mbNext.onClickDebounce(next)
        mtvSkip.onClickDebounce(skip)
    }

    companion object {
        fun newInstance(parent: ViewGroup, skip: () -> Unit, next: () -> Unit) = SplashViewHolder(parent.inflate(R.layout.item_splash), skip, next)
    }
}