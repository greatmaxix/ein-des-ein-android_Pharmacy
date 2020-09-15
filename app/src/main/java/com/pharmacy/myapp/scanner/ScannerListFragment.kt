package com.pharmacy.myapp.scanner

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pharmacy.myapp.R
import com.pharmacy.myapp.product.BaseProductFragment
import com.pharmacy.myapp.product.ProductViewModel
import kotlinx.android.synthetic.main.fragment_products_result.*

class ScannerListFragment(private val viewModel: ProductViewModel) : BaseProductFragment<ProductViewModel>(R.layout.fragment_scanner_result, viewModel) {

    private val args by navArgs<ScannerListFragmentArgs>()

    val adapter = ProductListScannerAdapter(viewModel::getProductInfo, viewModel::setOrRemoveWish)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        adapter.apply { setList(args.products.toMutableList()) }
        rvProducts.adapter = adapter
    }

    override fun notifyWish(globalProductId: Int) = adapter.notifyWish(globalProductId)

}