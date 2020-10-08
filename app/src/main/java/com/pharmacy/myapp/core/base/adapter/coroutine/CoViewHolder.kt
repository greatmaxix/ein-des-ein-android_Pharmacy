package com.pharmacy.myapp.core.base.adapter.coroutine

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.base.adapter.BaseViewHolderInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import timber.log.Timber

@KoinApiExtension
abstract class CoViewHolder<T>(containerView: View) : BaseViewHolder<T>(containerView), BaseViewHolderInterface, KoinComponent {

    private var viewHolderJob: CompletableJob? = null
    var viewHolderScope: CoroutineScope? = null
        private set

    override fun bind(item: T) {
        onViewRecycled()
        viewHolderJob = SupervisorJob()
        viewHolderScope = CoroutineScope(Main.immediate + viewHolderJob!!)
    }

    fun ImageView.setImageDrawableAsync(@DrawableRes drawableRes: Int, errorNotifier: ((Exception) -> Unit)? = null) = viewHolderScope?.launch {
        try {
            setImageDrawable(withContext(Default) { drawableRes.toDrawable })
        } catch (e: Exception) {
            Timber.e(e)
            errorNotifier?.invoke(e)
        }
    }

    fun View.setBackgroundAsync(@DrawableRes drawableRes: Int, errorNotifier: ((Exception) -> Unit)? = null) = viewHolderScope?.launch {
        try {
            background = withContext(Default) { drawableRes.toDrawable }
        } catch (e: Exception) {
            Timber.e(e)
            errorNotifier?.invoke(e)
        }
    }

    override fun onViewRecycled() {
        viewHolderJob?.cancel()
        viewHolderJob = null
        viewHolderScope = null
    }
}