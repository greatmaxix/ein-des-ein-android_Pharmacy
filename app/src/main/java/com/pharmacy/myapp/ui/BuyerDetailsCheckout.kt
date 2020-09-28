package com.pharmacy.myapp.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.debug
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.text
import com.pharmacy.myapp.data.remote.rest.request.order.CustomerOrderData
import com.pharmacy.myapp.ui.text.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_buyer_details_checkout.view.*

class BuyerDetailsCheckout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_buyer_details_checkout, true)

    private val sidePadding by lazy { resources.getDimension(R.dimen._16sdp).toInt() }
    private val bottomPadding by lazy { resources.getDimension(R.dimen._4sdp).toInt() }

    private var fullName: String? = null
    private var phone: String? = null
    private var email: String? = null

    init {
        debug { tilPhoneCheckout.prefixText = "+3" }
        val hint = if (BuildConfig.DEBUG) R.string.authPhoneDebugHint else R.string.authPhoneHint
        etPhoneCheckout.setAsteriskHint(context.getString(hint), 18, 19)

        tilFirstLastNameCheckout.editText?.doAfterTextChanged {
            hideError()
            fullName = it.toString()
        }
        tilPhoneCheckout.editText?.doAfterTextChanged {
            hideError()
            phone = tilPhoneCheckout.getPhonePrefix + it.toString()
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