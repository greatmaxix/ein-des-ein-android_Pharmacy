package com.pulse.ui

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.pulse.R
import com.pulse.core.extensions.*
import com.pulse.databinding.LayoutProfileItemBinding

class ProfileItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val binding = LayoutProfileItemBinding.inflate(inflater, this)

    var icon: Int = -1
        set(value) {
            field = value
            if (value != -1) binding.ivIcon.setImageResource(icon)
            else binding.ivIcon.setImageDrawable(null)
        }
    var title: String = ""
        set(value) {
            field = value
            binding.mtvTitle.text = value
        }
    var arrowVisibility: Boolean = true
        set(value) {
            field = value
            binding.ivArrow.visibleOrGone(value)
        }
    var mainColor: Int = -1
        set(value) {
            field = value
            binding.ivIcon.setColorFilter(ContextCompat.getColor(context, value), PorterDuff.Mode.SRC_IN)
            binding.mtvTitle.setTextColorRes(value)
        }
    var secondaryColor: Int = -1
        set(value) {
            field = value
            binding.ivIcon.backgroundTintList = ContextCompat.getColorStateList(context, value)
        }
    val selectColor: Int = R.color.primaryBlue
    var detailText: String = ""
        set(value) {
            field = value
            if (value.isNotEmpty()) {
                binding.mtvDetail.text = value
                binding.mtvDetail.visible()
            } else binding.mtvDetail.gone()
        }

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

    fun setOnClick(f: View.() -> Unit) = binding.clProfileItemContainer.setDebounceOnClickListener(listener = f)

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)

        with(binding) {
            binding.ivArrow.rotation = if (selected) -90f else 0f
            binding.ivArrow.setColorFilter(getColor(if (selected) selectColor else mainColor), PorterDuff.Mode.SRC_IN)
            binding.ivIcon.setColorFilter(getColor(if (selected) selectColor else mainColor), PorterDuff.Mode.SRC_IN)
            binding.mtvTitle.setTextColorRes(if (selected) selectColor else mainColor)
        }
    }
}