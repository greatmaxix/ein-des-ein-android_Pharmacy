package com.pharmacy.myapp.checkout.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.fragment.dialog.BaseDialogFragment
import com.pharmacy.myapp.core.extensions.hideKeyboard
import kotlinx.android.synthetic.main.dialog_promo_code.view.*

class PromoCodeDialogFragment : BaseDialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
        with(alertDialogBuilder) {
            setCancelable(false)
            setTitle(R.string.doYouHavePromoCode)
            setMessage(R.string.discountDescription)
            val customView = layoutInflater.inflate(R.layout.dialog_promo_code, null)
            setView(customView)
            setPositiveButton(R.string.promoCodeApply) { _, _ ->
                hideKeyboard()
                val code = customView.etPromoCode.text?.toString()?.trim().orEmpty()
                setFragmentResult(PROMO_CODE_REQUEST_KEY, bundleOf(PROMO_CODE_EXTRA_KEY to code))
            }
            setNegativeButton(R.string.promoCodeCancel) { _, _ -> dismiss() }
        }

        return alertDialogBuilder.create()
    }

    companion object {

        const val PROMO_CODE_REQUEST_KEY = "PROMO_CODE_REQUEST_KEY"
        const val PROMO_CODE_EXTRA_KEY = "PROMO_CODE_EXTRA_KEY"
    }
}