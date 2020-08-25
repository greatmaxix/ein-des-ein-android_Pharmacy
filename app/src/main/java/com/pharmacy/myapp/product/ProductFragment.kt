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

        fillTempData()

        productAnalogContainer.onClick { requireContext().toast("TODO: Analogs") }
        categoryContainer.onClick { requireContext().toast("TODO: Category") }
        instructionContainer.onClick { requireContext().toast("TODO: Instruction") }
        questionsContainer.onClick { requireContext().toast("TODO: Ask pharmacist") }
        favoriteFab.onClick { requireContext().toast("TODO: Favorite") }
        addToCartBtn.onClick { requireContext().toast("TODO: Add to cart") }

        bottomLayout.setTopRoundCornerBackground()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_share) {
            requireContext().toast("TODO: Share")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fillTempData() {
        productTitle.setTextHtml(args.product?.rusName)
        productShortDescription.setTextHtml(args.product?.releaseForm)
        //productIssuer.text = "Chinoin Pharmaceutical and Chemical Works Co.  Венгрия"

        tvPriceTo.text = getString(R.string.price, args.product?.aggregation?.maxPrice.toString())
        tvPriceFrom.text = getString(R.string.price, args.product?.aggregation?.minPrice.toString())

        analogTitle.text = "Аналоги и основное действующее вещетсво"
        analogName.text = "Дротаверин"

        categoryIcon.setImageResource(R.drawable.ic_vaccine)
        categoryName.text = "Противогрибковый"

        descriptionText.setTextHtml(args.product?.description)

        addToCartBtn.text = "В корзину от 568 ₽ -985 ₽"
    }
}