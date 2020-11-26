package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.pulse.R
import com.pulse.core.extensions.*
import com.pulse.data.remote.model.order.CustomerOrderData
import com.pulse.ui.text.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_buyer_details_checkout.view.*

class BuyerDetailsCheckout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView = inflate(R.layout.view_buyer_details_checkout, true)

    private val sidePadding by lazyDimensionPixelSize(R.dimen._16sdp)
    private val bottomPadding by lazyDimensionPixelSize(R.dimen._4sdp)

    private var fullName: String? = null
    private var phone: String? = null
    private var email: String? = null

    init {
        tilPhoneCheckout.fixPrefixGravity()
        debug { tilPhoneCheckout.prefixText = "+3" }
        val hint = debugIfElse({ R.string.authPhoneDebugHint }, { R.string.authPhoneHint })
        etPhoneCheckout.setAsteriskHint(context.getString(hint), 18, 19)

        tilFirstLastNameCheckout.editText?.doAfterTextChanged {
            hideError()
            fullName = it.toString()
        }
        tilPhoneCheckout.editText?.doAfterTextChanged {
            hideError()
            phone = tilPhoneCheckout.phoneCodePrefix + it.toString()
        }
        tilEmailCheckout.editText?.doAfterTextChanged {
            hideError()
            email = it.toString()
        }
        orientation = VERTICAL
        setPadding(sidePadding, 0, sidePadding, bottomPadding)
    }

    private fun hideError() {
        tilFirstLastNameCheckout.isErrorEnabled = false
        tilPhoneCheckout.isErrorEnabled = false
        tilEmailCheckout.isErrorEnabled = false
    }

    fun setData(fullName: String?, phone: String?, email: String?) {
        this.fullName = fullName
        this.phone = phone?.substring(2)
        this.email = email
        updateContent()
    }

    private fun updateContent() {
        tilFirstLastNameCheckout.editText?.setText(fullName)
        tilPhoneCheckout.editText?.setText(phone)
        tilEmailCheckout.editText?.setText(email)
    }

    fun isFieldsValid(): Boolean {
        val isNameValid = tilFirstLastNameCheckout.checkLength(context.getString(R.string.nameErrorAuth))
        val isPhoneValid = tilPhoneCheckout.isPhoneNumberValid(context.getString(R.string.phoneErrorAuth))
        val isEmailValid = if (tilEmailCheckout.text().isNotEmpty()) tilEmailCheckout.checkEmail(context.getString(R.string.emailErrorAuth)) else true
        val isValid = isNameValid && isPhoneValid && isEmailValid
        if (!isValid) requestFocus()
        return isValid
    }

    fun getDetail() = CustomerOrderData(fullName, phone, email)
}