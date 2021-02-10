package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.widget.LinearLayout
import com.pulse.R
import com.pulse.core.extensions.inflater
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.use
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.LayoutEmptyContainerBinding

class EmptyContainerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutEmptyContainerBinding.inflate(inflater, this)
    private var title = ""
    private var subtitle = ""
    private var buttonText = ""
    private var isButtonVisible = true
    private var src = -1

    init {
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.EmptyContainerView, defStyleAttr, -1)
                .use {
                    title = getString(R.styleable.EmptyContainerView_titleEmpty) ?: ""
                    subtitle = getString(R.styleable.EmptyContainerView_subtitleEmpty) ?: ""
                    buttonText = getString(R.styleable.EmptyContainerView_buttonTextEmpty) ?: ""
                    isButtonVisible = getBoolean(R.styleable.EmptyContainerView_isButtonVisible, true)
                    src = getResourceId(R.styleable.EmptyContainerView_src, -1)
                }
        }
        gravity = CENTER
        orientation = VERTICAL
    }

    override fun onFinishInflate() = with(binding) {
        super.onFinishInflate()
        if (src != -1) {
            ivPicture.setImageResource(src)
        }
        mtvEmptyTitle.text = title
        mtvEmptySubtitle.text = subtitle

        if (isButtonVisible && buttonText.isNotEmpty()) {
            mbSelect.text = buttonText
        }
        mbSelect.visibleOrGone(isButtonVisible)
    }

    fun setButtonAction(action: () -> Unit) = binding.mbSelect.setDebounceOnClickListener { action() }
}