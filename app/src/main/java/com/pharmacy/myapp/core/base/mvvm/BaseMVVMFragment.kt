package com.pharmacy.myapp.core.base.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.pharmacy.myapp.core.base.fragment.BaseFragment

abstract class BaseMVVMFragment(@LayoutRes private val layoutResourceId: Int) : BaseFragment(layoutResourceId) {

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

    protected fun <T, LD : LiveData<T>> observeNullable(liveData: LD, onChanged: (T?) -> Unit) {
        liveData.observe(viewLifecycleOwner, { onChanged(it) })
    }

    protected fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: (T) -> Unit) = liveData.observe(viewLifecycleOwner, { it?.let(onChanged) })

    protected fun <T> observeSavedStateHandler(key: String, onChanged: (T) -> Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)?.let { observe(it, onChanged) }
    }

    @Deprecated("User observe() member function")
    protected fun <T> LiveData<T>.observeSingle(onChanged: (T?) -> Unit) {
        observe(viewLifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                onChanged.invoke(t)
                removeObserver(this)
            }
        })
    }

    @Deprecated(
        "Use observe() member function, because we should invoke this class function and not use extension function",
        replaceWith = ReplaceWith("observe()", "com.pharmacy.myapp.core.base.mvvm")
    )
    protected fun <T> LiveData<T>.observeExt(onChanged: (T) -> Unit) {
        observe(viewLifecycleOwner, {
            it?.let(onChanged)
        })
    }

    @Deprecated(
        "Use observeNullable() member function, because we should invoke this class function and not use extension function",
        replaceWith = ReplaceWith("observeNullable()", "com.pharmacy.myapp.core.base.mvvm")
    )
    protected fun <T> LiveData<T>.observeNullableExt(onChanged: (T?) -> Unit) {
        observe(viewLifecycleOwner, {
            onChanged(it)
        })
    }
}