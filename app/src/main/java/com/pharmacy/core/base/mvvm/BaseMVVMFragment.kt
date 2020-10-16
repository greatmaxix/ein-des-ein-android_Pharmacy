package com.pharmacy.core.base.mvvm

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.lifecycle.LiveData
import com.pharmacy.core.base.fragment.BaseFragment
import com.pharmacy.core.dsl.ObserveGeneral
import com.pharmacy.core.network.Resource
import com.pharmacy.core.network.Resource.*

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

    protected fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: (T) -> Unit) = liveData.observe(viewLifecycleOwner, { it?.let(onChanged) })

    protected fun <T, LD : LiveData<T>> observeNullable(liveData: LD, onChanged: (T?) -> Unit) = liveData.observe(viewLifecycleOwner, { onChanged(it) })

    protected fun <T> observeResult(liveData: LiveData<Resource<T>>, block: (ObserveGeneral<T>.() -> Unit)? = null) {
        block?.let {
            ObserveGeneral<T>().apply(block).apply {
                observe(liveData) {
                    when (it) {
                        is Success<T> -> {
                            progressCallback?.setInProgress(false)
                            onEmmit(it.data)
                        }
                        is Progress -> {
                            onProgress?.invoke(it.isLoading) ?: progressCallback?.setInProgress(it.isLoading)
                        }
                        is Error -> {
                            progressCallback?.setInProgress(false)
                            onError?.invoke(it.exception) ?: messageCallback?.showError(it.exception.resId)
                        }
                    }
                }
            }
        } ?: observe(liveData) {
            when (it) {
                is Success<T> -> progressCallback?.setInProgress(false)
                is Progress -> progressCallback?.setInProgress(it.isLoading)
                is Error -> {
                    progressCallback?.setInProgress(false)
                    messageCallback?.showError(it.exception.resId)
                }
            }
        }
    }

    protected fun <T> observeSavedStateHandler(key: String, onChanged: (T) -> Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)?.let { observe(it, onChanged) }
    }
}