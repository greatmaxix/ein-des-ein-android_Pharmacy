package com.pharmacy.core.base.fragment.dialog

import androidx.annotation.StringRes

class AlertDialogDataRes(
    @StringRes var title: Int? = null,
    @StringRes var positive: Int? = null,
    @StringRes var negative: Int? = null,
    var positiveAction: (() -> Unit)? = null,
    var negativeAction: (() -> Unit)? = null,
    var cancelable: Boolean = true

)