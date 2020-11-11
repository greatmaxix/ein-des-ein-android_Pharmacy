package com.pulse.ui

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.pulse.R
import com.pulse.core.extensions.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_profile_item.view.*

class ProfileItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr), LayoutContainer {

    var icon: Int = -1
        set(value) {
            field = value
            if (value != -1) ivIconProfileItem.setImageResource(icon)
            else ivIconProfileItem.setImageDrawable(null)
        }
    var title: String = ""
        set(value) {
            field = value
            mtvTitleProfileItem.text = value
        }
    var arrowVisibility: Boolean = true
        set(value) {
            field = value
            ivArrowProfileItem.visibleOrGone(value)
        }
    var mainColor: Int = -1
        set(value) {
            field = value
            ivIconProfileItem.setColorFilter(ContextCompat.getColor(context, value), PorterDuff.Mode.SRC_IN)
            mtvTitleProfileItem.textColor(value)
        }
    var secondaryColor: Int = -1
        set(value) {
            field = value
            ivIconProfileItem.backgroundTintList = ContextCompat.getColorStateList(context, value)
        }
    val selectColor: Int = R.color.primaryBlue
    var detailText: String = ""
        set(value) {
            field = value
            if (value.isNotEmpty()) {
                mtvDetailProfileItem.text = value
                mtvDetailProfileItem.visible()
            } else mtvDetailProfileItem.gone()
        }
    override val containerView = inflate(R.layout.layout_profile_item, true)

    init {
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorGlobalWhite))
        radius = getDimensionPixelSize(R.dimen._7sdp).toFloat()
        cardElevation = 0F
        useCompatPadding = true
        setRippleColorResource(R.color.colorRippleBlue)
        attrs?.let {
            context.theme.obtainStyledAttributes(it, R.styleable.ProfileItemView, defStyleAttr, -1)
                .use {
                    icon = getResourceId(R.styleable.ProfileItemView_iconProfile, -1)
                    title = getString(R.styleable.ProfileItemView_titleProfile) ?: ""
                    detailText = getString(R.styleable.ProfileItemView_detailTextProfile) ?: ""
                    arrowVisibility = getBoolean(R.styleable.ProfileItemView_arrowVisibilityProfile, true)
                    mainColor = getResourceId(R.styleable.ProfileItemView_mainColorProfile, R.color.darkBlue)
                    secondaryColor = getResourceId(R.styleable.ProfileItemView_secondaryColorProfile, R.color.profileIconBackground)
                }
        }
    }

    fun setOnClick(f: View.() -> Unit) = profileItemContainer.setDebounceOnClickListener(listener = f)

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        ivArrowProfileItem.rotation = if (selected) -90f else 0f
        ivArrowProfileItem.setColorFilter(colorFrom(if (selected) selectColor else mainColor), PorterDuff.Mode.SRC_IN)
        ivIconProfileItem.setColorFilter(colorFrom(if (selected) selectColor else mainColor), PorterDuff.Mode.SRC_IN)
        mtvTitleProfileItem.textColor(if (selected) selectColor else mainColor)
    }
}