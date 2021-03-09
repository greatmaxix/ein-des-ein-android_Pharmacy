package com.pulse.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.pulse.R
import com.pulse.core.extensions.*
import com.pulse.data.remote.model.order.CustomerOrderData
import com.pulse.databinding.ViewBuyerDetailsCheckoutBinding
import com.pulse.ui.text.*

class BuyerDetailsCheckout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = ViewBuyerDetailsCheckoutBinding.inflate(inflater, this)
    private val sidePadding by lazyGetDimensionPixelSize(R.dimen._16sdp)
    private val bottomPadding by lazyGetDimensionPixelSize(R.dimen._4sdp)
    private val blueColor = getColor(R.color.primaryBlue)
    private val darkBlueColor = getColor(R.color.darkBlue)

    private var fullName: String? = null
    private var phone: String? = null
    private var email: String? = null
    private var delegateFullName: String? = null

    init {
        with(binding) {
            attrs?.let {
                context.theme.obtainStyledAttributes(it, R.styleable.BuyerDetailsView, defStyleAttr, -1)
                    .use {
                        mtvBuyerDetailsTitle.setText(getResourceId(R.styleable.BuyerDetailsView_labelText, R.string.buyerDetails))
                    }
            }

            tilPhone.fixPrefixGravity()
            debug { tilPhone.prefixText = "+3" }
            val hint = debugIfElse({ R.string.authPhoneDebugHint }, { R.string.authPhoneHint })
            etPhone.setAsteriskHint(context.getString(hint), 18, 19)
            tilFirstLastName.editText?.doAfterTextChanged {
                hideError()
                fullName = it.toString()
            }
            tilPhone.editText?.doAfterTextChanged {
                hideError()
                phone = tilPhone.phoneCodePrefix + it.toString()
            }
            tilEmail.editText?.doAfterTextChanged {
                hideError()
                delegateFullName = it.toString()
            }
            tilFirstLastNameDelegate.editText?.doAfterTextChanged {
                hideError()
                email = it.toString()
            }
            mtvDelegate.onClickDebounce {
                mtvDelegate.setCompoundDrawablesWithIntrinsicBounds(
                    if (tilFirstLastNameDelegate.isVisible) {
                        tilFirstLastNameDelegate.gone()
                        tilFirstLastNameDelegate.editText?.text = null
                        mtvDelegate.setTextColor(blueColor)
                        R.drawable.ic_plus_blue
                    } else {
                        tilFirstLastNameDelegate.visible()
                        mtvDelegate.setTextColor(darkBlueColor)
                        R.drawable.ic_minus_circle
                    },
                    0, 0, 0
                )
            }
        }
        orientation = VERTICAL
        setPadding(sidePadding, 0, sidePadding, bottomPadding)
    }

    private fun hideError() = with(binding) {
        tilFirstLastName.isErrorEnabled = false
        tilPhone.isErrorEnabled = false
        tilEmail.isErrorEnabled = false
        tilFirstLastNameDelegate.isErrorEnabled = false
    }

    fun setData(fullName: String?, phone: String?, email: String?, delegate: String? = null) {
        this.fullName = fullName
        this.phone = phone?.substring(2)
        this.email = email
        delegateFullName = delegate
        updateContent()
    }

    private fun updateContent() = with(binding) {
        tilFirstLastName.editText?.setText(fullName)
        tilPhone.editText?.setText(phone)
        tilEmail.editText?.setText(email)
        tilFirstLastNameDelegate.editText?.setText(delegateFullName)
    }

    fun isFieldsValid(): Boolean = with(binding) {
        val isNameValid = tilFirstLastName.checkLength(context.getString(R.string.nameErrorAuth))
        val isPhoneValid = tilPhone.isPhoneNumberValid(context.getString(R.string.phoneErrorAuth))
        val isEmailValid = if (tilEmail.text().isNotEmpty()) tilEmail.checkEmail(context.getString(R.string.emailErrorAuth)) else true
        val isValid = isNameValid && isPhoneValid && isEmailValid
        if (!isValid) requestFocus()
        return isValid
    }

    fun getDetail() = CustomerOrderData(fullName, phone, email, delegateFullName)
}