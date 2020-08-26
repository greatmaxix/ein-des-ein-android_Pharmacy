package com.pharmacy.myapp.product

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.setTextHtml
import com.pharmacy.myapp.core.extensions.setTopRoundCornerBackground
import com.pharmacy.myapp.core.extensions.toast
import com.pharmacy.myapp.product.adapter.ProductsImageAdapter
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.layout_product_card_additional_info.*
import kotlinx.android.synthetic.main.layout_product_card_image_pager.*
import kotlinx.android.synthetic.main.layout_product_card_main_info.*

class ProductFragment(private val viewModel: ProductViewModel) : BaseMVVMFragment(R.layout.fragment_product) {

    private val args: ProductFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        initMenu(R.menu.share)
        setToolbarTitle(args.product?.rusName)

        with(productImagePager) {
            args.product?.pictures?.let { adapter = ProductsImageAdapter(it) }
            TabLayoutMediator(productImagePagerIndicator, this) { _, _ -> }.attach()
        }

        setProductInfo()

        mcvAnalog.onClick { onAnalog() }
        mcvCategory.onClick { onCategory() }
        mcvInstruction.onClick { onInstruction() }
        mcvQuestions.onClick { onQuestions() }
        fbWish.onClick { onWish() }
        mbCart.onClick { onCart() }

        bottomLayout.setTopRoundCornerBackground()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_share) { requireContext().toast("TODO: Share") }
        return super.onOptionsItemSelected(item)
    }

    private fun onAnalog() { requireContext().toast("TODO: Analogs") }

    private fun onCategory() { requireContext().toast("TODO: Category") }

    private fun onInstruction() { requireContext().toast("TODO: Instruction") }

    private fun onQuestions() { requireContext().toast("TODO: Ask pharmacist") }

    private fun onWish() { requireContext().toast("TODO: Favorite") }

    private fun onCart() { requireContext().toast("TODO: Add to cart") }

    private fun setProductInfo() {
        args.product?.apply {
            tvTitle.setTextHtml(rusName)
            tvSubTitle.setTextHtml(releaseForm)
            tvManufacture.text = getFullManufacture
            tvPriceTo.text = getString(R.string.price, aggregation.maxPrice.toString())
            tvPriceFrom.text = getString(R.string.price, aggregation.minPrice.toString())
            tvAnalog.text = substance

            ivCategory.setImageResource(R.drawable.ic_vaccine)
            tvCategoryDes.text = category

            mtvDescriptionText.setTextHtml(args.product?.description)
        }
    }
}