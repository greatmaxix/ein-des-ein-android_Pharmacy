package com.pulse.scanner

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pulse.R
import com.pulse.product.BaseProductFragment
import com.pulse.product.ProductViewModel
import kotlinx.android.synthetic.main.fragment_scanner_result.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ScannerListFragment(private val viewModel: ProductViewModel) : BaseProductFragment<ProductViewModel>(R.layout.fragment_scanner_result, viewModel) {

    private val args by navArgs<ScannerListFragmentArgs>()

    private val adapter by lazy { ProductListScannerAdapter(viewModel::getProductInfo, viewModel::setOrRemoveWish, args.products.toMutableList()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        rvProducts.adapter = adapter
    }

    override fun notifyWish(globalProductId: Int) = adapter.notifyWish(globalProductId)

}