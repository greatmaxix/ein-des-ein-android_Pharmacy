package com.pharmacy.myapp.produtcList

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.myapp.MainGraphDirections
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.addAutoKeyboardCloser
import com.pharmacy.myapp.product.BaseProductFragment
import com.pharmacy.myapp.product.model.Product
import com.pharmacy.myapp.product.model.ProductLite
import com.pharmacy.myapp.search.SearchFragment
import com.pharmacy.myapp.search.SearchFragmentDirections
import com.pharmacy.myapp.search.SearchFragmentDirections.Companion.fromSearchToProduct
import com.pharmacy.myapp.search.SearchFragmentDirections.Companion.globalToProductCard
import com.pharmacy.myapp.user.wishlist.WishFragment
import timber.log.Timber
import java.lang.Exception

abstract class BaseProductListFragment<VM : BaseProductViewModel>(@LayoutRes private val layoutResourceId: Int, private val viewModel: VM) :
    BaseProductFragment<VM>(layoutResourceId, viewModel) {

    protected val productAdapter = ProductListAdapter(viewModel::getProductInfo, viewModel::setOrRemoveWish)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<RecyclerView>(R.id.rvProducts)?.apply {
            adapter = productAdapter
            addAutoKeyboardCloser()
        }

        setFragmentResultListener(PRODUCT_WISH_KEY) { key, bundle ->
            if (key == PRODUCT_WISH_KEY && bundle.containsKey(PRODUCT_WISH_FIELD)) {
                notifyWish(bundle.getInt(PRODUCT_WISH_FIELD))
            }
        }

        productAdapter.addLoadStateListener { progressCallback?.setInProgress(it.refresh is LoadState.Loading || it.append is LoadState.Loading) }
    }

    abstract val productLiveData: LiveData<PagingData<ProductLite>>

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(productLiveData) { productAdapter.submitData(lifecycle, it) }
        observe(viewModel.productLiteLiveData) { navController.navigate(getNavDirection(it)) }
    }

    private fun getNavDirection(product: Product) = when (this) {
        is WishFragment -> globalToProductCard(product)
        is SearchFragment -> fromSearchToProduct(product)
        else -> throw Exception("Add new instance to base product")
    }
}