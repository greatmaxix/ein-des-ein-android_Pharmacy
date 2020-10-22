package com.pulse.core.general.behavior

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.pulse.core.base.fragment.dialog.AlertDialogFragment
import com.pulse.core.extensions.DialogOnClick
import com.pulse.core.extensions.toast

class DialogMessagesBehavior private constructor() : IMessagesBehavior {

    private var context: Context? = null
    private var fragmentManager: FragmentManager? = null

    constructor(fragment: Fragment) : this() {
        fragmentManager = fragment.childFragmentManager
        context = fragment.context
    }

    constructor(activity: FragmentActivity) : this() {
        fragmentManager = activity.supportFragmentManager
        context = activity
    }

    override fun showMessage(message: String) {
        context?.toast(message)
    }

    override fun showMessage(@StringRes strResId: Int) {
        if (strResId != 0) {
            context?.toast(strResId)
        }
    }

    override fun showError(@StringRes strResId: Int, positiveAction: (() -> Unit)?) {
        if (strResId != 0) {
            context?.getString(strResId)?.let { showError(it, positiveAction) }
        }
    }

    override fun showError(error: String, positiveAction: (() -> Unit)?) {
        showError(message = error, positiveAction = positiveAction)
    }

    override fun showError(
        title: String?,
        message: String,
        isNeedNegative: Boolean,
        positiveButton: String?,
        negativeButton: String?,
        positiveAction: (() -> Unit)?
    ) {
        fragmentManager?.let {
            AlertDialogFragment.newInstance(title, message, positiveButton ?: context?.getString(android.R.string.ok), negativeButton)
                .apply {
                    setNegativeListener(DialogOnClick { dialog, _ -> dialog.dismiss() })
                    setPositiveListener(DialogOnClick { _, _ -> positiveAction?.invoke() })
                }.show(it)
        }
    }

    override fun detach() {
        context = null
        fragmentManager = null
    }
}