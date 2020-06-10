package com.pharmacy.myapp.core.base.adapter

import android.view.View
import com.pharmacy.myapp.core.extensions.subscribeEmpty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

abstract class BaseRxViewHolder<I : BaseRxItem<F>, F>(containerView: View) : BaseViewHolder<I>(containerView) {

    private var disposable: Disposable? = null

    abstract fun onConsumerReceiveData(data: F)

    override fun bind(item: I) {
        clear()
        disposable = item.flowable?.observeOn(AndroidSchedulers.mainThread())?.subscribeEmpty { onConsumerReceiveData(it) }
    }

    open fun clear() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        disposable = null
    }

}