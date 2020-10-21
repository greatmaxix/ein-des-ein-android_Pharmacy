package com.pulse.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.pulse.MainGraphDirections.Companion.globalToChat
import com.pulse.R
import com.pulse.core.extensions.*
import com.pulse.home.HomeFragmentDirections.Companion.fromHomeToScanner
import com.pulse.home.HomeFragmentDirections.Companion.globalToDevTools
import com.pulse.model.category.Category
import com.pulse.product.BaseProductFragment
import com.pulse.product.model.Product
import com.pulse.ui.CategoryHomeView
import com.pulse.ui.RecentlyViewedView
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class HomeFragment(private val viewModel: HomeViewModel) : BaseProductFragment<HomeViewModel>(R.layout.fragment_home, viewModel) {

    private var isCategoryLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mcvScanHome.onClick { doNav(fromHomeToScanner()) }
        mcvAskHome.onClick { doNav(globalToChat()) }
        mcvAnalyzeHome.onClick { navController.onNavDestinationSelected(R.id.nav_analyzes, null, R.id.nav_home) }
        uploadRecipes.onClick { navController.onNavDestinationSelected(R.id.nav_recipes, null, R.id.nav_home) }
        mcvSearchHome.onClick { navController.onNavDestinationSelected(R.id.nav_search, null, R.id.nav_home) }
        btnSeeAllCategoriesHome.onClick { navController.onNavDestinationSelected(R.id.nav_catalog, null, R.id.nav_home) }
        //mcvMapHome.onClick { navController.navigate(fromHomeToCheckout(true)) }
        mcvOrderContainer.onClick { }

        // Developers screen for convenient features access
        debug {
            mcvToolbarHome.setOnLongClickListener {
                navController.navigate(globalToDevTools())
                true
            }
        }
        viewModel.loadInitialData()
        progressBarCategories.visibleOrGone(!isCategoryLoaded)
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(viewModel.recentlyViewedLiveData, ::populateRecentViewed)
        observe(viewModel.categoriesLiveData, ::setCategories)
    }

    private fun setCategories(categories: List<Category>) {
        progressBarCategories.gone()
        llCategoriesContainer.visible()
        categories.forEachIndexed { index, category ->
            (llCategoriesContainer.getChildAt(index) as? CategoryHomeView)?.apply {
                setCategory(category)
                setDebounceOnClickListener {
                    navController.onNavDestinationSelected(R.id.nav_catalog, bundleOf("category" to category), R.id.nav_home)
                }
            }
        }
    }

    private fun populateRecentViewed(list: List<Product>) {
        if (list.isNotEmpty()) {
            llRecentlyViewedContainer.animateVisible()
            tvRecentlyViewedTitle.animateVisible()
            list.firstOrNull()?.let { setProduct(it, firstRecentlyViewedView) }
            list.getOrNull(1)?.let { setProduct(it, secondRecentlyViewedView) }
        }
    }

    private fun setProduct(product: Product, view: RecentlyViewedView) = with(view) {
        animateVisible()
        setProduct(product)
        onClick { viewModel.getProductInfo(product.globalProductId) }
    }

    override fun notifyWish(globalProductId: Int) {
        // mock
    }
}