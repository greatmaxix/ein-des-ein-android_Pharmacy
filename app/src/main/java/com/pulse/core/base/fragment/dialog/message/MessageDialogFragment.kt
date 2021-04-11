package com.pulse.core.base.fragment.dialog.message

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pulse.core.base.fragment.dialog.BaseDialogFragment
import com.pulse.core.extensions.DialogOnClick
import com.pulse.core.extensions.getStringArg
import com.pulse.core.extensions.putArgs

class MessageDialogFragment : BaseDialogFragment() {

    private var okListener: DialogOnClick? = null
    private var noListener: DialogOnClick? = null

    fun setPositiveListener(okListener: DialogOnClick) {
        this.okListener = okListener
    }

    fun setNegativeListener(noListener: DialogOnClick) {
        this.noListener = noListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = MaterialAlertDialogBuilder(requireContext())
        .apply {
            getStringArg(TITLE, null)?.let { setTitle(it) }
            getStringArg(MESSAGE, null)?.let { setMessage(it) }
            getStringArg(POSITIVE, null)?.let { setPositiveButton(it, okListener) }
            getStringArg(NEGATIVE, null)?.let { setNegativeButton(getStringArg(NEGATIVE, it), noListener) }
        }
        .create()

    companion object {

        private const val TITLE = "Title"
        private const val MESSAGE = "Message"
        private const val POSITIVE = "Positive"
        private const val NEGATIVE = "Negative"

        fun newInstance(title: String?, message: String?, positive: String?, negative: String?) =
            MessageDialogFragment().putArgs {
                putString(TITLE, title)
                putString(MESSAGE, message)
                putString(POSITIVE, positive)
                putString(NEGATIVE, negative)
            }
    }
}