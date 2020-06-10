package com.pharmacy.myapp.core.base.fragment.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pharmacy.myapp.core.extensions.getFragmentTag

abstract class BaseDialogFragment : DialogFragment() {
    open fun show(manager: FragmentManager) = super.show(manager, getFragmentTag())
}