package com.pharmacy.core.base.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import com.pharmacy.core.base.fragment.BaseBottomSheetDialogFragment
import androidx.lifecycle.Observer as Observer1

abstract class BaseMVVMFragmentDialogBottomSheet(@LayoutRes layoutResourceId: Int) : BaseBottomSheetDialogFragment(layoutResourceId) {

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindLiveData()
    }

    /**
     * Here we may bind our observers to LiveData if some.
     * This method will be executed after parent [onCreateView] method
     */
    protected open fun onBindLiveData() {
        //Optional
    }

    protected fun <T, LD : LiveData<T>> observeSingle(liveData: LD, onChanged: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner, object : Observer1<T> {
            override fun onChanged(t: T) {
                onChanged(t)
                liveData.removeObserver(this)
            }
        })
    }

    protected fun <T, LD : LiveData<T>> observeNullable(liveData: LD, onChanged: (T?) -> Unit) {
        liveData.observe(viewLifecycleOwner, { onChanged(it) })
    }

    protected fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: (T) -> Unit) = liveData.observe(viewLifecycleOwner, { it?.let(onChanged) })

}