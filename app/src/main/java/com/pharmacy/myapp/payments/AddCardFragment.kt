package com.pharmacy.myapp.payments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.isLetterAndSpace
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import com.pharmacy.myapp.core.extensions.toast
import com.pharmacy.myapp.util.CreditCardExpiryInputFilter
import com.pharmacy.myapp.util.CreditCardInputFilter
import kotlinx.android.synthetic.main.fragment_add_card.*

class AddCardFragment : PaymentsBaseFragment(R.layout.fragment_add_card) {

    private val stateListDefault by lazy { ContextCompat.getColorStateList(requireContext(), R.color.selector_text_input_default) }
    private val stateListAccepted by lazy { ContextCompat.getColorStateList(requireContext(), R.color.selector_text_input_accepted) }
    private val greyColor by lazy { R.color.grey.toColor }
    private val greenColor by lazy { R.color.acceptTextField.toColor }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton(R.drawable.ic_arrow_back)

        tilCardNumber.editText?.filters = arrayOf(LengthFilter(CREDIT_CARD_NUMBER_LENGTH), CreditCardInputFilter())
        tilCardNumber.editText?.doAfterTextChanged { text ->
            updateInputLayoutState(tilCardNumber, text.toString()) {
                it.length == CREDIT_CARD_NUMBER_LENGTH
            }
            updateButtonState()
        }
        tilCardNumber.setEndIconOnClickListener {
            if (tilCardNumber.editText?.text?.length != CREDIT_CARD_NUMBER_LENGTH) tilCardNumber.editText?.text = null
        }

        tilCardExpDate.editText?.doAfterTextChanged { text ->
            updateInputLayoutState(tilCardExpDate, text.toString()) {
                it.length == CREDIT_CARD_EXP_DATE_LENGTH
            }
            updateButtonState()
        }
        tilCardExpDate.editText?.filters = arrayOf<InputFilter>(CreditCardExpiryInputFilter())
        tilCardExpDate.setEndIconOnClickListener {
            if (tilCardExpDate.tag == null) {
                requireContext().toast("TODO show info") // TODO what to show
            } else {
                if (tilCardExpDate.editText?.text?.length != CREDIT_CARD_EXP_DATE_LENGTH) tilCardExpDate.editText?.text = null
            }
        }

        tilCardCVV.editText?.doAfterTextChanged { text ->
            updateInputLayoutState(tilCardCVV, text.toString()) {
                it.length == CREDIT_CARD_CVV_LENGTH
            }
            updateButtonState()
        }
        tilCardCVV.setEndIconOnClickListener {
            if (tilCardCVV.tag == null) {
                requireContext().toast("TODO show info") // TODO what to show
            } else {
                if (tilCardCVV.editText?.text?.length != CREDIT_CARD_CVV_LENGTH) tilCardCVV.editText?.text = null
            }
        }
        tilCardHolderName.setEndIconOnClickListener {

        }
        tilCardHolderName.editText?.doAfterTextChanged { text ->
            updateInputLayoutState(tilCardHolderName, text.toString()) {
                val textSplit = it.split(" ")
                it.isNotBlank() && it.isLetterAndSpace() && textSplit.size > 1 && textSplit[1].isNotBlank()
            }
            updateButtonState()
        }

        btnAddCard.setDebounceOnClickListener {
            requireContext().toast("TODO add card") // TODO add card
        }
    }

    private fun updateButtonState() {
        val cardNumberValid = tilCardNumber.tag != null && tilCardNumber.tag as Boolean
        val cardExpDateValid = tilCardExpDate.tag != null && tilCardExpDate.tag as Boolean
        val cardCVVValid = tilCardCVV.tag != null && tilCardCVV.tag as Boolean
        val cardCardHolderNameValid = tilCardHolderName.tag != null && tilCardHolderName.tag as Boolean
        btnAddCard.isEnabled = cardNumberValid && cardExpDateValid && cardCVVValid && cardCardHolderNameValid
    }

    private fun updateInputLayoutState(til: TextInputLayout, text: String, prediction: (String) -> Boolean) {
        val accepted = prediction.invoke(text)
        til.tag = accepted
        val currentColor = if (accepted) greenColor else greyColor
        val currentIcon = if (accepted) R.drawable.ic_check else R.drawable.ic_clear_search
        til.setEndIconDrawable(currentIcon)
        til.setEndIconTintList(ColorStateList.valueOf(currentColor))
        til.isEndIconVisible = text.isNotEmpty()
        val stateList = if (accepted) stateListAccepted else stateListDefault
        stateList?.let { til.setBoxStrokeColorStateList(it) }
    }

    companion object {

        private const val CREDIT_CARD_NUMBER_LENGTH = 19
        private const val CREDIT_CARD_EXP_DATE_LENGTH = 5
        private const val CREDIT_CARD_CVV_LENGTH = 3
    }
}