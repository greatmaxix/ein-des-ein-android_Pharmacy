package com.pharmacy.myapp.core.base.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.pharmacy.myapp.core.base.fragment.BaseBottomSheetDialogFragment
import com.pharmacy.myapp.core.extensions.getFragmentTag

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

    protected fun <T> LiveData<T>.observeSingle(onChanged: (T?) -> Unit) {
        observe(viewLifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                onChanged.invoke(t)
                removeObserver(this)
            }
        })
    }

    protected fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer {
            it?.let(onChanged)
        })
    }

    protected fun <T> LiveData<T>.observeNullable(onChanged: (T?) -> Unit) {
        observe(viewLifecycleOwner, Observer {
            onChanged(it)
        })
    }

    @CallSuper
    open fun show(manager: FragmentManager) = super.show(manager, getFragmentTag())

}