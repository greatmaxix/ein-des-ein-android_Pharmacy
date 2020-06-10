package com.pharmacy.myapp.core.general.behavior

import androidx.annotation.StringRes

interface IMessagesBehavior : IBehavior {

    fun showError(error: String, positiveAction: (() -> Unit)? = null)

    fun showError(title: String? = null, message: String, isNeedNegative: Boolean = false, positiveButton: String? = null, negativeButton: String? = null, positiveAction: (() -> Unit)? = null)

    fun showError(@StringRes strResId: Int, positiveAction: (() -> Unit)? = null)

    fun showMessage(@StringRes strResId: Int)

    fun showMessage(message: String)

}