package com.pulse.components.productList

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.pulse.R
import com.pulse.components.product.BaseProductFragment
import com.pulse.components.product.BaseProductViewModel
import com.pulse.components.product.model.ProductLite
import com.pulse.components.productList.adapter.ProductListAdapter
import com.pulse.core.extensions.addAutoKeyboardCloser
import com.pulse.core.extensions.addStateListener
import com.pulse.core.extensions.observe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinApiExtension
import kotlin.reflect.KClass

@KoinApiExtension
@ExperimentalCoroutinesApi
abstract class BaseProductListFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, viewModelClass: KClass<VM>) :
    BaseProductFragment<VM>(layoutResourceId, viewModelClass) {

    protected val productAdapter by lazy { ProductListAdapter(viewModel::getProductInfo, viewModel::setOrRemoveWish) }
    abstract val pagedSearchState: Flow<PagingData<ProductLite>>

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
        productAdapter.addStateListener { uiHelper.showLoading(it) }
    }


    override fun notifyWish(globalProductId: Int) = productAdapter.notifyWish(globalProductId)

    override fun onBindStates() = with(lifecycleScope) {
        observe(pagedSearchState) { productAdapter.submitData(lifecycle, it) }
    }
}