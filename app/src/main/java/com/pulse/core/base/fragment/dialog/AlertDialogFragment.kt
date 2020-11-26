package com.pulse.core.base.fragment.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.pulse.core.extensions.DialogOnClick
import com.pulse.core.extensions.getStringArg
import com.pulse.core.extensions.putArgs

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

        fun newInstance(message: String, block: AlertDialogData.() -> Unit) = AlertDialogData()
            .apply(block)
            .run {
                newInstance(title, message, positive, negative)
                    .apply {
                        setNegativeListener { dialog, _ ->
                            negativeAction?.invoke()
                            dialog.dismiss()
                        }
                        setPositiveListener { _, _ -> positiveAction?.invoke() }
                        isCancelable = cancelable
                    }
            }

        fun newInstance(context: Context, message: String, block: AlertDialogDataRes.() -> Unit) = AlertDialogDataRes()
            .apply(block)
            .run {
                newInstance(title?.let(context::getString), message, positive?.let(context::getString), negative?.let(context::getString))
                    .apply {
                        setNegativeListener { dialog, _ ->
                            negativeAction?.invoke()
                            dialog.dismiss()
                        }
                        setPositiveListener { _, _ -> positiveAction?.invoke() }
                        isCancelable = cancelable
                    }
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