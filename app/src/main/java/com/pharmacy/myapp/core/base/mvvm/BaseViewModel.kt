package com.pharmacy.myapp.core.base.mvvm

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val disposables = CompositeDisposable()

    /**
     * This method adds given rx disposables to the [disposables]
     * which is unsubscribed [onCleared]
     *
     * @param disposable - rx disposables that must be unsubscribed in [onCleared]
     */
    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    protected fun dispose() {
        disposables.dispose()
    }

    protected fun removeDisposable(disposable: Disposable) {
        disposables.remove(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}