package com.pulse.components.checkout.dialog

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.dialogViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pulse.R
import com.pulse.core.base.fragment.dialog.BaseDialogFragment
import com.pulse.core.extensions.hideKeyboard
import com.pulse.databinding.DialogPromoCodeBinding

class PromoCodeDialogFragment : BaseDialogFragment() {

    private val binding by dialogViewBinding(DialogPromoCodeBinding::bind, R.id.fl_container)

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialAlertDialogBuilder(requireContext())
            .setView(R.layout.dialog_promo_code)
            .setCancelable(false)
            .setTitle(R.string.doYouHavePromoCode)
            .setMessage(R.string.discountDescription)
            .setPositiveButton(R.string.promoCodeApply) { _, _ ->
                hideKeyboard()
                // TODO not working and crashing   java.lang.IllegalArgumentException: parameter must be a descendant of this view
                val code = binding.etPromoCode.toString().trim()
                setFragmentResult(PROMO_CODE_REQUEST_KEY, bundleOf(PROMO_CODE_EXTRA_KEY to code))
            }
            .setNegativeButton(R.string.promoCodeCancel) { _, _ -> dismiss() }
            .create()

    companion object {

        const val PROMO_CODE_REQUEST_KEY = "PROMO_CODE_REQUEST_KEY"
        const val PROMO_CODE_EXTRA_KEY = "PROMO_CODE_EXTRA_KEY"
    }
}