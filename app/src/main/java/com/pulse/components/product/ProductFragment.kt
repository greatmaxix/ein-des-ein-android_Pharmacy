package com.pulse.components.product

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.product.ProductFragmentDirections.Companion.fromProductToPharmacy
import com.pulse.components.product.adapter.ProductsImageAdapter
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentProductBinding
import com.pulse.util.ColorFilterUtil.blackWhiteFilter
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ProductFragment(private val viewModel: ProductViewModel) : BaseProductFragment<ProductViewModel>(R.layout.fragment_product, viewModel) {

    private val args: ProductFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentProductBinding::bind)

    //TODO uncomment when @clearFragmentResult@ will be work correctly
    private var isNeedResult = false
    /* set(value) {
         field = value
         if (field)
             setFragmentResult(PRODUCT_WISH_KEY, bundleOf(PRODUCT_WISH_FIELD to args.product.globalProductId))
         else
             clearFragmentResult(PRODUCT_WISH_FIELD)
        }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton { backPress() }
        setToolbarTitle(args.product.rusName)

        with(layoutProductCardImagePager) {
            vpProductImages.adapter = ProductsImageAdapter(args.product.pictures)
            TabLayoutMediator(tabsIndicator, vpProductImages) { _, _ -> }.attach()
            if (args.product.pictures.isEmpty()) ivProductDetailAbsent.visible()
            if (args.product.pictures.size < 2) tabsIndicator.gone()
        }

        setProductInfo()

        with(layoutProductCardMainInfo) {
            mcvAnalog.mockToast()
            mcvCategory.mockToast()
        }
        with(layoutProductCardAdditionalInfo) {
            mcvInstruction.mockToast()
            mcvQuestions.setDebounceOnClickListener { viewModel.performAskPharmacist() }
        }
        fabWish.setDebounceOnClickListener { viewModel.setOrRemoveWish(!args.product.isInWish to args.product.globalProductId) }
        args.product.aggregation?.let {
            mbToPharmacy.setDebounceOnClickListener { navController.navigate(fromProductToPharmacy(args.product.globalProductId)) }
        } ?: run {
            mbToPharmacy.isEnabled = false
        }

        llBottom.setTopRoundCornerBackground()
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
        with(binding.layoutProductCardMainInfo) {
            mtvTitle.setTextHtml(rusName)
            mtvSubtitle.setTextHtml(releaseForm)
            mtvManufacture.text = getFullManufacture
            aggregation?.let {
                mtvPriceTo.text = getString(R.string.price, aggregation?.maxPrice?.formatPrice())
                mtvPriceFrom.text = getString(R.string.price, aggregation?.minPrice?.formatPrice())
            } ?: run {
                binding.layoutProductCardImagePager.ivProductDetailAbsent.colorFilter = blackWhiteFilter
            }
            groupPriceFields.visibleOrGone(aggregation != null)
            mtvPriceUnavailable.visibleOrGone(aggregation == null)
            mtvAnalog.text = substance
            mcvAnalog.animateVisibleOrGoneIfNot(substance != null)
            ivCategory.setImageResource(R.drawable.ic_vaccine)
            mtvCategoryDescription.text = category
        }
        binding.layoutProductCardAdditionalInfo.mtvDescriptionText.setTextHtml(description)
        binding.fabWish.setWish(isInWish)
    }

    override fun notifyWish(globalProductId: Int) {
        isNeedResult = !isNeedResult
        args.product.wish = !args.product.isInWish
        binding.fabWish.setWish(args.product.isInWish)
    }

    override fun needToLogin() = navController.navigate(R.id.fromProductToAuth, SignInFragmentArgs(R.id.nav_product).toBundle())

    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(viewModel.directionLiveData, ::doNav)
    }
}