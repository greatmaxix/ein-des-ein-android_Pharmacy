package com.pulse.components.scanner

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.product.BaseProductFragment
import com.pulse.components.product.ProductViewModel
import com.pulse.components.scanner.adapter.ProductListScannerAdapter
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.visible
import com.pulse.databinding.FragmentScannerResultBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ScannerListFragment(private val viewModel: ProductViewModel) : BaseProductFragment<ProductViewModel>(R.layout.fragment_scanner_result, viewModel) {

    private val args by navArgs<ScannerListFragmentArgs>()
    private val binding by viewBinding(FragmentScannerResultBinding::bind)
    private val adapter by lazy {
        ProductListScannerAdapter(
            viewModel::getProductInfo,
            viewModel::setOrRemoveWish,
            args.products.toMutableList()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        if (args.products.isEmpty()) {
            viewEmptyProducts.visible()
            viewEmptyProducts.setButtonAction { navigationBack() }
            rvProducts.gone()
        }
        rvProducts.adapter = adapter
    }

    override fun notifyWish(globalProductId: Int) = adapter.notifyWish(globalProductId)
}