package com.pharmacy.myapp.core.general.interfaces

import androidx.annotation.StringRes

interface MessagesCallback {

    fun showError(error: String, action: (() -> Unit)? = null)

    fun showError(@StringRes strResId: Int, action: (() -> Unit)? = null)

}