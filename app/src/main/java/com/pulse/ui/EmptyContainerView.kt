package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import com.pulse.R
import com.pulse.core.extensions.inflater
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.visibleOrGone
import com.pulse.databinding.LayoutEmptyContainerBinding

class EmptyContainerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = LayoutEmptyContainerBinding.inflate(inflater, this)
    private var title = ""
    private var subtitle = ""
    private var buttonText = ""
    private var isButtonVisible = true
    private var src = -1

    init {
        attrs?.let { attrsSet ->
            context.theme.obtainStyledAttributes(attrsSet, R.styleable.EmptyContainerView, defStyleAttr, -1)
                .use {
                    title = it.getString(R.styleable.EmptyContainerView_titleEmpty) ?: ""
                    subtitle = it.getString(R.styleable.EmptyContainerView_subtitleEmpty) ?: ""
                    buttonText = it.getString(R.styleable.EmptyContainerView_buttonTextEmpty) ?: ""
                    isButtonVisible = it.getBoolean(R.styleable.EmptyContainerView_isButtonVisible, true)
                    src = it.getResourceId(R.styleable.EmptyContainerView_src, -1)
                }
        }
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