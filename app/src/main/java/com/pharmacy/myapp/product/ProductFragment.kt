package com.pharmacy.myapp.product

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.SignInFragmentArgs
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.product.adapter.ProductsImageAdapter
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
        initMenu(R.menu.share)
        setToolbarTitle(args.product.rusName)

        with(productImagePager) {
            adapter = ProductsImageAdapter(args.product.pictures)
            TabLayoutMediator(productImagePagerIndicator, this) { _, _ -> }.attach()
        }

        setProductInfo()

        mcvAnalog.onClick { onAnalog() }
        mcvCategory.onClick { onCategory() }
        mcvInstruction.onClick { onInstruction() }
        mcvQuestions.onClick(::onQuestions)
        fbWish.onClick(::onWish)
        mbCart.onClick { onCart() }

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

    override fun onResume() {
        super.onResume()
        viewModel.checkIsWishSaved()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_share) {
            requireContext().toast("TODO: Share")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onAnalog() {
        requireContext().toast("TODO: Analogs")
    }

    private fun onCategory() {
        requireContext().toast("TODO: Category")
    }

    private fun onInstruction() {
        requireContext().toast("TODO: Instruction")
    }

    private fun onQuestions() = navController.navigate(R.id.fromProductToChat)

    private fun onWish() = viewModel.setOrRemoveWish(!args.product.isWish to args.product.globalProductId)

    private fun onCart() {
        requireContext().toast("TODO: Add to cart")
    }

    private fun setProductInfo() = with(args.product) {
        tvTitle.setTextHtml(rusName)
        tvSubTitle.setTextHtml(releaseForm)
        tvManufacture.text = getFullManufacture
        tvPriceTo.text = getString(R.string.price, aggregation.maxPrice.toString())
        tvPriceFrom.text = getString(R.string.price, aggregation.minPrice.toString())
        tvAnalog.text = substance

        ivCategory.setImageResource(R.drawable.ic_vaccine)
        tvCategoryDes.text = category

        mtvDescriptionText.setTextHtml(description)

        fbWish.notifyWish(isWish)
    }

    private fun FloatingActionButton.notifyWish(isWish: Boolean) {
        setImageResource(if (isWish) R.drawable.ic_heart_fill else R.drawable.ic_heart_stroke)
    }

    override fun notifyWish(globalProductId: Int) {
        isNeedResult = !isNeedResult
        args.product.wish = !args.product.isWish
        fbWish.notifyWish(args.product.isWish)
    }

    override fun needToLogin() {
        navController.navigate(R.id.fromProductToAuth, SignInFragmentArgs(R.id.nav_product).toBundle())
    }
}