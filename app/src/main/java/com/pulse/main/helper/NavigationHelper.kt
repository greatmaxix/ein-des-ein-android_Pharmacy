package com.pulse.main.helper

import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupWithNavController
import com.pulse.R
import com.pulse.components.user.model.customer.Customer
import com.pulse.core.extensions.*
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.ui.SelectableBottomNavView
import timber.log.Timber

class NavigationHelper(private val activity: FragmentActivity) : INavigationHelper {

    private val topLevelDestinations = intArrayOf(R.id.nav_home, R.id.nav_catalog, R.id.nav_search, R.id.nav_cart, R.id.nav_profile, R.id.nav_guest_profile)
    private val authDestinations = intArrayOf(R.id.nav_sign_in, R.id.nav_sign_up, R.id.nav_code)
    private val onboardingDestinations = intArrayOf(R.id.nav_on_boarding)
    private val splashDestinations = intArrayOf(R.id.nav_splash)
    private val NavDestination.isAuthDestination
        get() = authDestinations.contains(id)
    private val NavDestination.isOnboardingDestination
        get() = onboardingDestinations.contains(id)
    private val NavDestination.isSplashDestination
        get() = splashDestinations.contains(id)
    private val NavDestination.isTopLevelDestination
        get() = topLevelDestinations.contains(id)
    private val NavDestination.isTopDestinationAndHome
        get() = isTopLevelDestination && id == R.id.nav_home
    private val NavDestination.isCategories
        get() = id == R.id.nav_catalog
    private val navController by activity.lazyNavController()
    private val bottomNavigationView: SelectableBottomNavView by lazy { activity.findViewById(R.id.bottom_navigation) }

    override val deepLinkGraphDestination = R.navigation.graph_profile to R.id.nav_language

    override val onDestinationChangedFlow = StateEventFlow<NavDestination?>(null)
    override val onMenuItemChangedFlow = StateEventFlow<Int?>(null)

    init {
        with(bottomNavigationView) {
            itemIconTintList = null
            setupWithNavController(navController)
            setOnNavigationItemSelectedListener {
                onMenuItemChangedFlow.postState(it.itemId)
                true
            }
            setOnNavigationItemReselectedListener {}

            navController.addOnDestinationChangedListener { _, destination, _ ->
                Timber.d("Destination ID > ${resources.getResourceEntryName(destination.id)}")
                val isLightStatusBar = destination.isAuthDestination || destination.isOnboardingDestination
                val statusBarColor = when {
                    destination.isAuthDestination -> R.color.colorGlobalWhite
                    destination.isOnboardingDestination -> R.color.colorGreyLight
                    destination.isSplashDestination -> R.color.darkBlue
                    else -> R.color.primaryBlue
                }
                activity.setStatusBarColor(statusBarColor)
                activity.setLightStatusBar(isLightStatusBar)

                if (destination.isTopLevelDestination) showNav() else hideNav()
                val isChatForeground = destination.id == R.id.nav_chat
                onDestinationChangedFlow.postState(destination)
                activity.window.setSoftInputMode(
                    if (isChatForeground) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                )
                if (bottomNavigationView.navItems.isNotEmpty()) changeSelection(destination.id)
            }
        }
    }

    override fun setBottomNavItems(customer: Customer?) = with(bottomNavigationView) {
        val avatarUrl = customer?.avatarInfo?.url.orEmpty()
        bottomNavigationView.navItems = listOf(
            SelectableBottomNavView.NavItem(R.id.nav_home, R.id.nav_home, R.drawable.ic_home, null),
            SelectableBottomNavView.NavItem(R.id.nav_catalog, R.id.nav_catalog, R.drawable.ic_catalog, null),
            SelectableBottomNavView.NavItem(R.id.nav_search, R.id.nav_search, R.drawable.ic_search, null, isFab = true),
            SelectableBottomNavView.NavItem(R.id.nav_cart, R.id.nav_cart, R.drawable.ic_shopping_cart, null),
            SelectableBottomNavView.NavItem(R.id.profile_graph, R.id.nav_profile, null, avatarUrl, isProfileItem = true)
        )

        clearIntentExtrasIfHas()
    }

    private fun clearIntentExtrasIfHas() {
        activity.intent.apply {
            removeExtraIfHas(DEEP_LINK_IDS)
            removeExtraIfHas(DEEP_LINK_INTENT)
        }
    }

    override fun onBackPress(superBackPress: () -> Unit) {

        fun finish() = activity.finish()

        navController.currentDestination?.apply {
            when {
                isCategories -> superBackPress()
                isTopDestinationAndHome -> finish()
                isTopLevelDestination -> navController.navigate(R.id.nav_home)
                else -> superBackPress()
            }
        } ?: superBackPress()
    }

    companion object {

        private const val DEEP_LINK_IDS = "android-support-nav:controller:deepLinkIds"
        private const val DEEP_LINK_INTENT = "android-support-nav:controller:deepLinkIntent"
    }
}