package com.pulse.product

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.core.extensions.*
import com.pulse.product.ProductFragmentDirections.Companion.fromProductToPharmacy
import com.pulse.product.adapter.ProductsImageAdapter
import com.pulse.util.ColorFilterUtil.blackWhiteFilter
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.layout_product_card_additional_info.*
import kotlinx.android.synthetic.main.layout_product_card_image_pager.*
import kotlinx.android.synthetic.main.layout_product_card_main_info.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
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

        mcvAnalog.mockToast(getString(R.string.expectSoonMock))
        mcvCategory.mockToast("TODO: Category")
        mcvInstruction.mockToast(getString(R.string.expectSoonMock))
        mcvQuestions.setDebounceOnClickListener { navController.navigate(R.id.fromProductToChat) }
        fbWish.setDebounceOnClickListener { viewModel.setOrRemoveWish(!args.product.isInWish to args.product.globalProductId) }
        args.product.aggregation?.let {
            mbToPharmacy.setDebounceOnClickListener { navController.navigate(fromProductToPharmacy(args.product.globalProductId)) }
        } ?: run {
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

    private fun setProductInfo() = with(args.product) {
        tvTitle.setTextHtml(rusName)
        tvSubTitle.setTextHtml(releaseForm)
        tvManufacture.text = getFullManufacture
        aggregation?.let {
            tvPriceTo.text = getString(R.string.price, aggregation?.maxPrice?.formatPrice())
            tvPriceFrom.text = getString(R.string.price, aggregation?.minPrice?.formatPrice())
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