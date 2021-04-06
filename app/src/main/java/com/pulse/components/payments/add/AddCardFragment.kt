package com.pulse.components.payments.add

import android.content.res.ColorStateList
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputLayout
import com.pulse.R
import com.pulse.components.payments.PaymentsViewModel
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.getColor
import com.pulse.core.extensions.isLetterAndSpace
import com.pulse.core.extensions.mockToast
import com.pulse.core.extensions.toast
import com.pulse.databinding.FragmentAddCardBinding
import com.pulse.util.CreditCardExpiryInputFilter
import com.pulse.util.CreditCardInputFilter

class AddCardFragment : BaseToolbarFragment<PaymentsViewModel>(R.layout.fragment_add_card, PaymentsViewModel::class) { // TODO set proper view model

    private val binding by viewBinding(FragmentAddCardBinding::bind)
    private val stateListDefault by lazy { ContextCompat.getColorStateList(requireContext(), R.color.selector_text_input_default) }
    private val stateListAccepted by lazy { ContextCompat.getColorStateList(requireContext(), R.color.selector_text_input_accepted) }
    private val greyColor by lazy { getColor(R.color.grey) }
    private val greenColor by lazy { getColor(R.color.acceptTextField) }

    override fun initUI() = with(binding) {
        showBackButton()
        with(tilCardNumber) {
            editText?.filters = arrayOf(LengthFilter(CREDIT_CARD_NUMBER_LENGTH), CreditCardInputFilter())
            updateStateAfterTextChanged {
                it.length == CREDIT_CARD_NUMBER_LENGTH
            }
            setEndIconOnClickListener {
                if (tilCardNumber.editText?.text?.length != CREDIT_CARD_NUMBER_LENGTH) tilCardNumber.editText?.text = null
            }
        }
        with(tilExpDate) {
            editText?.filters = arrayOf<InputFilter>(CreditCardExpiryInputFilter())
            updateStateAfterTextChanged {
                it.length == CREDIT_CARD_EXP_DATE_LENGTH
            }
            setEndIconOnClickListener {
                if (tilExpDate.tag == null) {
                    requireContext().toast("TODO show info") // TODO what to show
                } else if (tilExpDate.tag as Boolean) {
                    if (tilExpDate.editText?.text?.length != CREDIT_CARD_EXP_DATE_LENGTH) tilExpDate.editText?.text = null
                }
            }
        }
        with(tilCvv) {
            updateStateAfterTextChanged {
                it.length == CREDIT_CARD_CVV_LENGTH
            }
            setEndIconOnClickListener {
                if (tilCvv.tag == null) {
                    requireContext().toast("TODO show info") // TODO what to show
                } else {
                    if (tilCvv.editText?.text?.length != CREDIT_CARD_CVV_LENGTH) tilCvv.editText?.text = null
                }
            }
        }
        with(tilCardHolderName) {
            updateStateAfterTextChanged {
                val textSplit = it.split(" ")
                it.isNotBlank() && it.isLetterAndSpace() && textSplit.size > 1 && textSplit[1].isNotBlank()
            }
            setEndIconOnClickListener {
                if (tag != null && !(tag as Boolean)) {
                    editText?.text = null
                }
            }
        }

        mbAddCard.mockToast()
    }

    private fun updateButtonState() = with(binding) {
        val cardNumberValid = tilCardNumber.tag != null && tilCardNumber.tag as Boolean
        val cardExpDateValid = tilExpDate.tag != null && tilExpDate.tag as Boolean
        val cardCVVValid = tilCvv.tag != null && tilCvv.tag as Boolean
        val cardCardHolderNameValid = tilCardHolderName.tag != null && tilCardHolderName.tag as Boolean
        mbAddCard.isEnabled = cardNumberValid && cardExpDateValid && cardCVVValid && cardCardHolderNameValid
    }

    private fun TextInputLayout.updateStateAfterTextChanged(prediction: (String) -> Boolean) {
        editText?.doAfterTextChanged { editable ->
            val text = editable.toString()
            val accepted = prediction(text)
            tag = accepted
            val currentColor = if (accepted) greenColor else greyColor
            val currentIcon = if (accepted) R.drawable.ic_check else R.drawable.ic_clear_search
            setEndIconDrawable(currentIcon)
            setEndIconTintList(ColorStateList.valueOf(currentColor))
            isEndIconVisible = text.isNotEmpty()
            val stateList = if (accepted) stateListAccepted else stateListDefault
            stateList?.let { setBoxStrokeColorStateList(it) }

            updateButtonState()
        }
    }

    companion object {

        private const val CREDIT_CARD_NUMBER_LENGTH = 19
        private const val CREDIT_CARD_EXP_DATE_LENGTH = 5
        private const val CREDIT_CARD_CVV_LENGTH = 3
    }
}