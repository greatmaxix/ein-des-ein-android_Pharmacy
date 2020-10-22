package com.pulse.core.base.fragment.dialog

class AlertDialogData(
    var title: String? = null,
    var positive:String? = null,
    var negative: String? = null,
    var positiveAction: (() -> Unit)? = null,
    var negativeAction: (() -> Unit)? = null,
    var cancelable: Boolean = true

)