package com.pharmacy.myapp.core.base.adapter.rx

import android.view.View
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.base.adapter.BaseViewHolderInterface
import com.pharmacy.myapp.core.extensions.subscribeEmpty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

abstract class BaseRxViewHolder<I : BaseRxItem<F>, F>(containerView: View) : BaseViewHolder<I>(containerView), BaseViewHolderInterface {

    private var disposable: Disposable? = null

    abstract fun onConsumerReceiveData(data: F)

    override fun bind(item: I) {
        onViewRecycled()
        disposable = item.flowable?.observeOn(AndroidSchedulers.mainThread())?.subscribeEmpty { onConsumerReceiveData(it) }
    }

    override fun onViewRecycled() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        disposable = null
    }
}