package com.pharmacy.myapp.ui

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_profile_item.view.*

class ProfileItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LayoutContainer {

    private var icon: Int = -1
    private var title: String = ""
    private var detailText: String = ""
    private var arrowVisibility: Boolean = true
    private var mainColor: Int = -1

    override val containerView = inflate(R.layout.layout_profile_item, true)

    init {
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.ProfileItemView, defStyleAttr, -1)
                .getData {
                    icon = getResourceId(R.styleable.ProfileItemView_iconProfile, -1)
                    title = getString(R.styleable.ProfileItemView_titleProfile)?:""
                    detailText = getString(R.styleable.ProfileItemView_detailTextProfile)?:""
                    arrowVisibility = getBoolean(R.styleable.ProfileItemView_arrowVisibilityProfile, true)
                    mainColor = getResourceId(R.styleable.ProfileItemView_mainColorProfile, R.color.darkBlue)
                }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ivIconProfileItem.setImageResource(icon)
        mtvTitleProfileItem.text = title
        mtvDetailProfileItem.text = detailText
        ivArrowProfileItem.visibleOrGone(arrowVisibility)
        ivIconProfileItem.setColorFilter(ContextCompat.getColor(context, mainColor), PorterDuff.Mode.SRC_IN)
        mtvTitleProfileItem.textColor(mainColor)
    }

    fun setOnClick(f: () -> Unit) = profileItemContainer.onClick(f)

}