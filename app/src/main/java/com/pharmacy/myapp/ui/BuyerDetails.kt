package com.pharmacy.myapp.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.animateVisibleOrGone
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.onClick
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_buyer_details.view.*

class BuyerDetails @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_buyer_details, true)

    private var editMode: Boolean? = null
        set(value) {
            field = value
            if (value == null) return
            updateContent()
        }

    var fullName: String? = null
        private set
    var phone: String? = null
        private set
    var email: String? = null
        private set

    init {
        tilFirstLastNameCheckout.editText?.doAfterTextChanged { fullName = it.toString() }
        tilPhoneCheckout.editText?.doAfterTextChanged { phone = it.toString() }
        tilEmailCheckout.editText?.doAfterTextChanged { email = it.toString() }
        tvBuyerDetailsEditCheckout.onClick { editMode = true }
    }

    fun setData(fullName: String?, phone: String?, email: String?) {
        this.fullName = fullName
        this.phone = phone
        this.email = email
        editMode = fullName.isNullOrBlank() || phone.isNullOrBlank() || email.isNullOrBlank()
        updateContent()
    }

    private fun updateContent() {
        val isEditMode = editMode != false
        tvBuyerDetailsTitleCheckout.text = context.getString(if (isEditMode) R.string.yourDetails else R.string.buyerDetails)
        if (editMode != false) {
            tilFirstLastNameCheckout.editText?.setText(fullName)
            tilPhoneCheckout.editText?.setText(phone)
            tilEmailCheckout.editText?.setText(email)
        } else {
            tvBuyerFullNameCheckout.text = fullName
            val phoneHolder = "\uD83D\uDCF1 $phone"
            tvBuyerPhoneCheckout.text = phoneHolder
            val emailHolder = "\uD83D\uDCEA $email"
            tvBuyerEmailCheckout.text = emailHolder
        }
        groupInputFieldsCheckout.animateVisibleOrGone(isEditMode)
        groupFilledDetailsCheckout.animateVisibleOrGone(!isEditMode)
    }
}