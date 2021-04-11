package com.pulse.core.base.helper

import android.app.Activity
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pulse.R
import com.pulse.core.base.fragment.dialog.message.MessageDialogData
import com.pulse.core.base.fragment.dialog.message.MessageDialogFragment
import com.pulse.core.extensions.openSettings
import timber.log.Timber

class UiHelper private constructor(
    private val activity: Activity,
    private val fragmentManager: FragmentManager
) : IUiHelper {

    constructor(fragment: Fragment) : this(fragment.requireActivity(), fragment.childFragmentManager)

    constructor(activity: FragmentActivity) : this(activity, activity.supportFragmentManager)

    private var loadingDialog: AlertDialog? = null
    val loadingDialogView by lazy { activity.layoutInflater.inflate(R.layout.view_loading_dialog, null) }
    private val anim: AnimatedVectorDrawable?
        get() = loadingDialogView.findViewById<ImageView>(R.id.iv_loader)?.drawable?.run { this as AnimatedVectorDrawable }

    override fun showLoading(show: Boolean) {
        Timber.d("Loading:> $show")
        if (show) showLoadingDialog() else hideLoadingDialog()
    }

    private fun showLoadingDialog() = loadingDialog?.apply {
        show()
        startAnimation()
    } ?: buildLoading()

    private fun hideLoadingDialog() = loadingDialog?.apply {
        anim?.clearAnimationCallbacks()
        dismiss()
    }

    private fun startAnimation() = anim?.apply {
        registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                start()
            }
        })
        start()
    }

    private fun buildLoading() {
        loadingDialog = MaterialAlertDialogBuilder(activity)
            .setView(loadingDialogView)
            .setCancelable(false)
            .create()
            .apply {
                window?.requestFeature(FEATURE_NO_TITLE)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                show()
                startAnimation()
            }
    }

    override fun showMessage(message: String) {
        val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }

    override fun showDialog(message: String, block: (MessageDialogData.() -> Unit)) = buildDialog(message, block).show(fragmentManager)

    private fun buildDialog(message: String, block: MessageDialogData.() -> Unit) = MessageDialogData()
        .apply(block)
        .run {
            MessageDialogFragment.newInstance(title, message, positive, negative)
                .apply {
                    setNegativeListener { _, _ -> negativeAction?.invoke() }
                    setPositiveListener { _, _ -> positiveAction?.invoke() }
                    isCancelable = cancelable
                }
        }

    fun openSettingsMessage(message: String) = showDialog(message) {
        cancelable = false
        positive = activity.getString(R.string.common_permissionDialog_settingsButton)
        positiveAction = { activity.openSettings() }
        negative = activity.getString(R.string.cancel)
    }
}