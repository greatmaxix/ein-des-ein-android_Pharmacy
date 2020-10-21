package com.pulse.core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior.State
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pulse.core.extensions.getFragmentTag
import com.pulse.core.general.behavior.IBehavior

abstract class BaseBottomSheetDialogFragment(@LayoutRes private val layoutResourceId: Int) : BottomSheetDialogFragment() {

    protected val bottomSheetBehavior by lazy { (dialog as BottomSheetDialog).behavior }

    private val behaviors = mutableListOf<IBehavior?>()

    protected fun <T : IBehavior> attachBehavior(behavior: T) = behavior.also { behaviors.add(it) }

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(layoutResourceId, container, false)

    @CallSuper
    override fun onDestroy() {
        behaviors.forEach { it?.detach() }
        behaviors.clear()
        super.onDestroy()
    }

    protected fun setState(@State state: Int) {
        bottomSheetBehavior.state = state
    }

    @CallSuper
    open fun show(manager: FragmentManager) {
        val tag = getFragmentTag()
        if (manager.findFragmentByTag(tag) == null) {
            super.show(manager, tag)
        }
    }
}