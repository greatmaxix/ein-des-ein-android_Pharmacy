package com.pulse.payments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.pulse.R
import com.pulse.core.extensions.isLetterAndSpace
import com.pulse.core.extensions.mockToast
import com.pulse.core.extensions.toast
import com.pulse.util.CreditCardExpiryInputFilter
import com.pulse.util.CreditCardInputFilter
import kotlinx.android.synthetic.main.fragment_add_card.*

class AddCardFragment : PaymentsBaseFragment(R.layout.fragment_add_card) {

    private val stateListDefault by lazy { ContextCompat.getColorStateList(requireContext(), R.color.selector_text_input_default) }
    private val stateListAccepted by lazy { ContextCompat.getColorStateList(requireContext(), R.color.selector_text_input_accepted) }
    private val greyColor by lazy { R.color.grey.toColor }
    private val greenColor by lazy { R.color.acceptTextField.toColor }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(tilCardNumber) {
            editText?.filters = arrayOf(LengthFilter(com.pulse.payments.AddCardFragment.CREDIT_CARD_NUMBER_LENGTH), CreditCardInputFilter())
            updateStateAfterTextChanged {
                it.length == com.pulse.payments.AddCardFragment.CREDIT_CARD_NUMBER_LENGTH
            }
            setEndIconOnClickListener {
                if (tilCardNumber.editText?.text?.length != com.pulse.payments.AddCardFragment.CREDIT_CARD_NUMBER_LENGTH) tilCardNumber.editText?.text = null
            }
        }
        with(tilCardExpDate) {
            editText?.filters = arrayOf<InputFilter>(CreditCardExpiryInputFilter())
            updateStateAfterTextChanged {
                it.length == com.pulse.payments.AddCardFragment.CREDIT_CARD_EXP_DATE_LENGTH
            }
            setEndIconOnClickListener {
                if (tilCardExpDate.tag == null) {
                    requireContext().toast("TODO show info") // TODO what to show
                } else if (tilCardExpDate.tag as Boolean) {
                    if (tilCardExpDate.editText?.text?.length != com.pulse.payments.AddCardFragment.CREDIT_CARD_EXP_DATE_LENGTH) tilCardExpDate.editText?.text = null
                }
            }
        }
        with(tilCardCVV) {
            updateStateAfterTextChanged {
                it.length == com.pulse.payments.AddCardFragment.CREDIT_CARD_CVV_LENGTH
            }
            setEndIconOnClickListener {
                if (tilCardCVV.tag == null) {
                    requireContext().toast("TODO show info") // TODO what to show
                } else {
                    if (tilCardCVV.editText?.text?.length != com.pulse.payments.AddCardFragment.CREDIT_CARD_CVV_LENGTH) tilCardCVV.editText?.text = null
                }
            }
        }
        with(tilCardHolderName) {
            updateStateAfterTextChanged {
                val textSplit = it.split(" ")
                it.isNotBlank() && it.isLetterAndSpace() && textSplit.size > 1 && textSplit[1].isNotBlank()
            }
            setEndIconOnClickListener {
                if (tilCardHolderName.tag != null && !(tilCardHolderName.tag as Boolean)) {
                    tilCardHolderName.editText?.text = null
                }
            }
        }

        btnAddCard.mockToast("TODO add card") // TODO add card

    }

    private fun updateButtonState() {
        val cardNumberValid = tilCardNumber.tag != null && tilCardNumber.tag as Boolean
        val cardExpDateValid = tilCardExpDate.tag != null && tilCardExpDate.tag as Boolean
        val cardCVVValid = tilCardCVV.tag != null && tilCardCVV.tag as Boolean
        val cardCardHolderNameValid = tilCardHolderName.tag != null && tilCardHolderName.tag as Boolean
        btnAddCard.isEnabled = cardNumberValid && cardExpDateValid && cardCVVValid && cardCardHolderNameValid
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