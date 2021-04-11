package com.pulse.core.base.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.pulse.core.base.fragment.dialog.BaseDialogFragment

abstract class BaseMVVMFragmentDialog(@LayoutRes val resId: Int) : BaseDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(resId, container, false)
}