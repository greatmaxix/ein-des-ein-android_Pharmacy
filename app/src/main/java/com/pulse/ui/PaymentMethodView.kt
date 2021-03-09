package com.pulse.ui

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import com.google.android.material.card.MaterialCardView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.pulse.R
import com.pulse.components.checkout.model.PaymentMethodModel
import com.pulse.components.payments.model.PaymentMethod
import com.pulse.core.extensions.*
import com.pulse.util.ColorFilterUtil

class PaymentMethodView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    private val padding by lazyGetDimensionPixelSize(R.dimen._8sdp)
    private val cornerRadius by lazyGetDimension(R.dimen._10sdp)
    private val backgroundColor by lazy { getColor(R.color.colorGlobalWhite) }
    private val radioButtonPadding by lazy { getDimensionPixelSize(R.dimen._8sdp) }
    private val radioButtonLayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    private val radioGroup by lazy {
        RadioGroup(context).apply {
            dividerPadding = padding
            val divider = ShapeDrawable()
            divider.paint.color = getColor(R.color.grey)
            divider.intrinsicHeight = 1
            divider.setPadding(0, padding, 0, padding)
            dividerDrawable = divider
            showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        }
    }
    private val radioIdMap = hashMapOf<Int, PaymentMethodModel>()

    init {
        radius = cornerRadius
        setContentPadding(padding, padding, padding, padding)
        cardElevation = 0f
        maxCardElevation = 0f
        setCardBackgroundColor(backgroundColor)
        PaymentMethod.values()
            .map { PaymentMethodModel(it, it == PaymentMethod.CASH) }
            .forEach {
                val button = createPaymentRadioButton(it)
                radioIdMap[button.id] = it
                radioGroup.addView(button)
            }
        addView(radioGroup)
    }

    private fun createPaymentRadioButton(it: PaymentMethodModel) = MaterialRadioButton(context).apply {
        id = generateViewId()
        this.layoutParams = radioButtonLayoutParams
        setPadding(radioButtonPadding, (radioButtonPadding * 1.5).toInt(), radioButtonPadding, (radioButtonPadding * 1.5).toInt())
        text = context.getString(it.method.title)
        val drawable = context.getDrawable(it.method.icon)
            ?.apply { if (!it.isChecked) colorFilter = ColorFilterUtil.blackWhiteFilter }
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        setTextColor(getColorStateList(R.color.selector_text_payment))
        buttonTintList = getColorStateList(R.color.selector_tint_button_payment)
        isEnabled = it.isChecked
        isChecked = it.isChecked
    }

    fun getSelectedPaymentMethodModel() = radioIdMap[radioGroup.checkedRadioButtonId]
}