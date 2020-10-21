package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.widget.LinearLayout
import com.pulse.R
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.onClick
import com.pulse.core.extensions.use
import com.pulse.core.extensions.visibleOrGone
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_empty_container.view.*

class EmptyContainerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr),
    LayoutContainer {

    override val containerView = inflate(R.layout.layout_empty_container, true)

    private var title = ""
    private var subtitle = ""
    private var buttonText = ""
    private var isButtonVisible = true

    init {
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.EmptyContainerView, defStyleAttr, -1)
                .use {
                    title = getString(R.styleable.EmptyContainerView_titleEmpty) ?: ""
                    subtitle = getString(R.styleable.EmptyContainerView_subtitleEmpty) ?: ""
                    buttonText = getString(R.styleable.EmptyContainerView_buttonTextEmpty) ?: ""
                    isButtonVisible = getBoolean(R.styleable.EmptyContainerView_isButtonVisible, true)
                }
        }
        gravity = CENTER
        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        titleEmptyContainer.text = title
        subtitleEmptyContainer.text = subtitle

        with(actionEmptyContainer) {
            visibleOrGone(isButtonVisible)
            if (isButtonVisible && buttonText.isNotEmpty()) {
                text = buttonText
            }
        }
    }

    fun setButtonAction(action: () -> Unit) = actionEmptyContainer.onClick { action() }

}