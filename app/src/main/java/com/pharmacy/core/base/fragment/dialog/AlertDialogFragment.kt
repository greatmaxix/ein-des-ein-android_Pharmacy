package com.pharmacy.core.base.fragment.dialog

import android.app.AlertDialog
import android.os.Bundle
import androidx.annotation.NonNull
import com.pharmacy.core.extensions.DialogOnClick
import com.pharmacy.core.extensions.getStringArg
import com.pharmacy.core.extensions.putArgs

class AlertDialogFragment : BaseDialogFragment() {

    companion object {

        private const val TITLE = "Title"
        private const val MESSAGE = "Message"
        private const val POSITIVE = "Positive"
        private const val NEGATIVE = "Negative"

        @JvmOverloads
        fun newInstance(title: String? = null, message: String?, positive: String?, negative: String?) =
            AlertDialogFragment().putArgs {
                putString(TITLE, title)
                putString(MESSAGE, message)
                putString(POSITIVE, positive)
                putString(NEGATIVE, negative)
            }
    }

    private var okListener: DialogOnClick? = null
    private var noListener: DialogOnClick? = null

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog = AlertDialog.Builder(context)
            .apply {
                getStringArg(TITLE, null)?.let { setTitle(it) }
                getStringArg(MESSAGE, null)?.let { setMessage(it) }
                getStringArg(POSITIVE, null)?.let { setPositiveButton(it, okListener) }
                getStringArg(NEGATIVE, null)?.let {
                    setNegativeButton(getStringArg(NEGATIVE, it), noListener)
                }
            }.create()

    fun setPositiveListener(okListener: DialogOnClick) {
        this.okListener = okListener
    }

    fun setNegativeListener(noListener: DialogOnClick) {
        this.noListener = noListener
    }
}