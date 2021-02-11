package com.pulse.components.productList

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.pulse.R
import com.pulse.components.product.BaseProductFragment
import com.pulse.components.product.BaseProductViewModel
import com.pulse.components.product.model.ProductLite
import com.pulse.components.productList.adapter.ProductListAdapter
import com.pulse.core.extensions.addAutoKeyboardCloser
import com.pulse.core.extensions.addStateListener
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
abstract class BaseProductListFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModel: VM) :
    BaseProductFragment<VM>(layoutResourceId, viewModel) {

    protected val productAdapter = ProductListAdapter(viewModel::getProductInfo, viewModel::setOrRemoveWish)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.rv_products)?.apply {
            adapter = productAdapter
            addAutoKeyboardCloser()
        }

        setFragmentResultListener(PRODUCT_WISH_KEY) { key, bundle ->
            if (key == PRODUCT_WISH_KEY && bundle.containsKey(PRODUCT_WISH_FIELD)) {
                notifyWish(bundle.getInt(PRODUCT_WISH_FIELD))
            }
        }

        productAdapter.addStateListener { progressCallback?.setInProgress(it) }
    }

    abstract val productLiveData: LiveData<PagingData<ProductLite>>

    override fun notifyWish(globalProductId: Int) = productAdapter.notifyWish(globalProductId)

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(productLiveData) { productAdapter.submitData(lifecycle, it) }
    }
}