package com.pharmacy.myapp.product

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentArgs
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.product.ProductFragmentDirections.Companion.fromProductToPharmacy
import com.pharmacy.myapp.product.adapter.ProductsImageAdapter
import com.pharmacy.myapp.util.ColorFilterUtil.blackWhiteFilter
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.layout_product_card_additional_info.*
import kotlinx.android.synthetic.main.layout_product_card_image_pager.*
import kotlinx.android.synthetic.main.layout_product_card_main_info.*

class ProductFragment(private val viewModel: ProductViewModel) : BaseProductFragment<ProductViewModel>(R.layout.fragment_product, viewModel) {

    private val args: ProductFragmentArgs by navArgs()

    //TODO uncomment when @clearFragmentResult@ will be work correctly
    private var isNeedResult = false
    /* set(value) {
         field = value
         if (field)
             setFragmentResult(PRODUCT_WISH_KEY, bundleOf(PRODUCT_WISH_FIELD to args.product.globalProductId))
         else
             clearFragmentResult(PRODUCT_WISH_FIELD)
        }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton { backPress() }
        setToolbarTitle(args.product.rusName)

        with(productImagePager) {
            adapter = ProductsImageAdapter(args.product.pictures)
            TabLayoutMediator(productImagePagerIndicator, this) { _, _ -> }.attach()
            if (args.product.pictures.isEmpty()) ivProductDetailAbsent.visible()
        }

        setProductInfo()

        mcvAnalog.setDebounceOnClickListener { onAnalog() }
        mcvCategory.setDebounceOnClickListener { onCategory() }
        mcvInstruction.setDebounceOnClickListener { onInstruction() }
        mcvQuestions.setDebounceOnClickListener { navController.navigate(R.id.fromProductToChat) }
        fbWish.setDebounceOnClickListener { viewModel.setOrRemoveWish(!args.product.isInWish to args.product.globalProductId) }
        args.product.aggregation?.let {
            mbToPharmacy.setDebounceOnClickListener { navController.navigate(fromProductToPharmacy(args.product.globalProductId)) }
        }?: run {
            mbToPharmacy.isEnabled = false
        }

        bottomLayout.setTopRoundCornerBackground()

        addBackPressListener { backPress() }
    }

    //TODO remove it after correct work of @clearFragmentResult@
    private fun backPress() {
        if (isNeedResult) {
            setFragmentResult(PRODUCT_WISH_KEY, bundleOf(PRODUCT_WISH_FIELD to args.product.globalProductId))
        }
        navController.popBackStack()
    }

    private fun onAnalog() = requireContext().toast(getString(R.string.expectSoonMock))

    private fun onCategory() = requireContext().toast("TODO: Category")

    private fun onInstruction() = requireContext().toast(getString(R.string.expectSoonMock))

    private fun setProductInfo() = with(args.product) {
        tvTitle.setTextHtml(rusName)
        tvSubTitle.setTextHtml(releaseForm)
        tvManufacture.text = getFullManufacture
        aggregation?.let {
            tvPriceTo.text = getString(R.string.price, aggregation?.maxPrice.toString())
            tvPriceFrom.text = getString(R.string.price, aggregation?.minPrice.toString())
        } ?: run {
            ivProductDetailAbsent.colorFilter = blackWhiteFilter
        }
        groupPriceFields.visibleOrGone(aggregation != null)
        tvPriceUnavailable.visibleOrGone(aggregation == null)

        tvAnalog.text = substance

        ivCategory.setImageResource(R.drawable.ic_vaccine)
        tvCategoryDes.text = category

        mtvDescriptionText.setTextHtml(description)

        fbWish.setWish(isInWish)
    }

    override fun notifyWish(globalProductId: Int) {
        isNeedResult = !isNeedResult
        args.product.wish = !args.product.isInWish
        fbWish.setWish(args.product.isInWish)
    }

    override fun needToLogin() = navController.navigate(R.id.fromProductToAuth, SignInFragmentArgs(R.id.nav_product).toBundle())
}