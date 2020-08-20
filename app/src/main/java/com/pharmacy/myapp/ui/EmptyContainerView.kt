package com.pharmacy.myapp.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.getData
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.onClick
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_empty_container.view.*

class EmptyContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView = inflate(R.layout.layout_empty_container, true)

    private var title = ""
    private var subtitle = ""
    private var buttonText = ""

    init {
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.EmptyContainerView, defStyleAttr, -1)
                .getData {
                    title = getString(R.styleable.EmptyContainerView_titleEmpty) ?: ""
                    subtitle = getString(R.styleable.EmptyContainerView_subtitleEmpty) ?: ""
                    buttonText = getString(R.styleable.EmptyContainerView_buttonTextEmpty) ?: ""
                }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        titleEmptyContainer.text = title
        subtitleEmptyContainer.text = subtitle
        actionEmptyContainer.text = buttonText
    }

    fun setButtonAction(action: () -> Unit) = actionEmptyContainer.onClick { action() }

}