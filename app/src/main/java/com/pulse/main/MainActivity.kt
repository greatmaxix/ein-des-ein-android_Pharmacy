package com.pulse.main

import android.os.Bundle
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupWithNavController
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMActivity
import com.pulse.core.extensions.hideNav
import com.pulse.core.extensions.onNavDestinationSelected
import com.pulse.core.extensions.setTopRoundCornerBackground
import com.pulse.core.extensions.showNav
import com.pulse.core.general.behavior.DialogMessagesBehavior
import com.pulse.core.general.behavior.ProgressViewBehavior
import com.pulse.core.general.interfaces.MessagesCallback
import com.pulse.core.general.interfaces.ProgressCallback
import com.pulse.ui.SelectableBottomNavView
import com.pulse.user.model.customer.Customer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_progress.*

class MainActivity : BaseMVVMActivity<MainViewModel>(R.layout.activity_main, MainViewModel::class), ProgressCallback, MessagesCallback {

    private val progressBehavior by lazy { attachBehavior(ProgressViewBehavior(progressRoot)) }
    private val messagesBehavior by lazy { attachBehavior(DialogMessagesBehavior(this)) }

    private val topLevelDestinations = intArrayOf(R.id.nav_home, R.id.nav_catalog, R.id.nav_search, R.id.nav_cart, R.id.nav_profile, R.id.nav_guest_profile)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    override fun onBindLiveData() {
        observe(viewModel.directionLiveData) { navController.onNavDestinationSelected(this) }
        observeNullable(viewModel.customerInfoLiveData, ::setBottomNavItems)
    }

    private fun setBottomNavItems(customer: Customer?) {
        val avatarUrl = customer?.avatarInfo?.url.orEmpty()
        bottomNavigation.navItems = listOf(
            SelectableBottomNavView.NavItem(R.id.nav_home, R.id.nav_home, R.drawable.ic_home, null),
            SelectableBottomNavView.NavItem(R.id.nav_catalog, R.id.nav_catalog, R.drawable.ic_catalog, null),
            SelectableBottomNavView.NavItem(R.id.nav_search, R.id.nav_search, R.drawable.ic_search, null, true),
            SelectableBottomNavView.NavItem(R.id.nav_cart, R.id.nav_cart, R.drawable.ic_shopping_cart, null),
            SelectableBottomNavView.NavItem(R.id.profile_graph, R.id.nav_profile, null, avatarUrl)
        )
    }

    override fun setInProgress(progress: Boolean) = progressBehavior.setInProgress(progress)

    override fun showError(error: String, action: (() -> Unit)?) = messagesBehavior.showError(error, action)

    override fun showError(strResId: Int, action: (() -> Unit)?) = messagesBehavior.showError(strResId, action)

    override fun onBackPressed() {
        navController.currentDestination?.apply {
            when {
                isCategories -> super.onBackPressed()
                isTopDestinationAndHome -> finish()
                isTopLevelDestination -> navController.navigate(R.id.nav_home)
                else -> super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }

    private fun setupNavigation() = with(bottomNavigation) {
        setTopRoundCornerBackground()
        itemIconTintList = null
        setupWithNavController(navController)
        setBottomNavItems(viewModel.customerInfoLiveData.value)
        setOnNavigationItemSelectedListener {
            viewModel.navSelected(it.itemId)
            true
        }
        setOnNavigationItemReselectedListener {}
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.isTopLevelDestination) showNav() else hideNav()
            changeSelection(destination)
        }
    }

    private val NavDestination.isTopLevelDestination
        get() = topLevelDestinations.contains(id)

    private val NavDestination.isTopDestinationAndHome
        get() = isTopLevelDestination && id == R.id.nav_home

    private val NavDestination.isCategories
        get() = id == R.id.nav_catalog
}