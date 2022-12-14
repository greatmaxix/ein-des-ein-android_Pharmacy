package com.pulse.components.home

import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.home.HomeFragmentDirections.Companion.fromHomeToScanner
import com.pulse.components.home.HomeFragmentDirections.Companion.fromHomeToSearch
import com.pulse.components.home.HomeFragmentDirections.Companion.globalToStub
import com.pulse.components.product.BaseProductFragment
import com.pulse.components.product.model.Product
import com.pulse.components.stub.model.StubType
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentHomeBinding
import com.pulse.model.category.Category
import com.pulse.ui.CategoryHomeView
import com.pulse.ui.RecentlyViewedView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
@ExperimentalCoroutinesApi
class HomeFragment : BaseProductFragment<HomeViewModel>(R.layout.fragment_home, HomeViewModel::class) {

    private var isCategoryLoaded = false
    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun initUI() = with(binding) {
        setSoftInputMode(SOFT_INPUT_ADJUST_PAN)

        mcvScan.setDebounceOnClickListener { navController.navigate(fromHomeToScanner()) }
        mcvAsk.setDebounceOnClickListener { viewModel.performAskPharmacist() }
        mcvAnalyze.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_analyze_category, null, R.id.nav_home) }
        mbUploadRecipes.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_recipes, null, R.id.nav_home) }
        mcvSearch.setDebounceOnClickListener { navController.navigate(fromHomeToSearch()) }
        mbSeeAllCategories.setDebounceOnClickListener { navController.onNavDestinationSelected(R.id.nav_catalog, null, R.id.nav_home) }
        mcvIataTravel.setDebounceOnClickListener { navController.navigate(globalToStub(StubType.IATA_TRAVEL_PASS)) }

        viewModel.loadInitialData()
        pbCategories.visibleOrGone(!isCategoryLoaded)
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.recentlyViewedState, ::populateRecentViewed)
        observe(viewModel.categoriesState, ::setCategories)
    }

    private fun setCategories(categories: List<Category>) = with(binding) {
        pbCategories.gone()
        llCategoriesContainer.visible()
        categories.forEachIndexed { index, category ->
            (llCategoriesContainer.getChildAt(index) as? CategoryHomeView)?.apply {
                setCategory(category.name to index)
                this.setDebounceOnClickListener {
                    navController.onNavDestinationSelected(R.id.nav_catalog, bundleOf("category" to category), R.id.nav_home)
                }
            }
        }
    }

    private fun populateRecentViewed(list: List<Product>) = with(binding) {
        if (list.isNotEmpty()) {
            llRecentlyViewedContainer.animateVisible()
            mtvRecentlyViewedTitle.animateVisible()
            list.firstOrNull()?.let { setProduct(it, viewFirstRecentlyViewed) }
            list.getOrNull(1)?.let { setProduct(it, viewSecondRecentlyViewed) }
        }
    }

    private fun setProduct(product: Product, view: RecentlyViewedView) = with(view) {
        animateVisible()
        setProduct(product)
        this.setDebounceOnClickListener { viewModel.getProductInfo(product.globalProductId) }
    }

    override fun notifyWish(globalProductId: Int) {
        // mock
    }
}