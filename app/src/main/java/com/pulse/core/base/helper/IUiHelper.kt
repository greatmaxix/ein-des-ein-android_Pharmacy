package com.pulse.core.base.helper

import com.pulse.core.base.fragment.dialog.message.MessageDialogData

interface IUiHelper {

    fun showLoading(show: Boolean)

    fun showMessage(message: String)

    fun showDialog(message: String, block: (MessageDialogData.() -> Unit) = {})
}