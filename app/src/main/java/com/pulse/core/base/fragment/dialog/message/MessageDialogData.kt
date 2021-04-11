package com.pulse.core.base.fragment.dialog.message

data class MessageDialogData(
    var title: String? = null,
    var positive: String? = null,
    var negative: String? = null,
    var positiveAction: (() -> Unit)? = null,
    var negativeAction: (() -> Unit)? = null,
    var cancelable: Boolean = true
)