package com.pharmacy.myapp.core.base.mvvm

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pharmacy.myapp.core.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseMVVMActivity<out VM : ViewModel>(@LayoutRes layoutResourceId: Int, viewModelClass: KClass<VM>) : BaseActivity(layoutResourceId) {

    protected val viewModel: VM by lazy { getViewModel(clazz = viewModelClass) }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBindLiveData()
    }

    /**
     * Here we may bind our observers to LiveData if some.
     * This method will be executed after parent [onCreate] method
     */
    protected open fun onBindLiveData() {
        //Optional
    }

    protected fun <T, LD : LiveData<T>> observeNullable(liveData: LD, onChanged: T?.() -> Unit) {
        liveData.observe(this, { value ->
            onChanged(value)
        })
    }

    protected fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: T.() -> Unit) {
        liveData.observe(this, { value ->
            value?.let(onChanged)
        })
    }
}