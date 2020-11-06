package com.pulse.main

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.LiveData
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupWithNavController
import com.pulse.MainGraphDirections.Companion.globalToChat
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMActivity
import com.pulse.core.dsl.ObserveGeneral
import com.pulse.core.extensions.*
import com.pulse.core.general.behavior.DialogMessagesBehavior
import com.pulse.core.general.behavior.ProgressViewBehavior
import com.pulse.core.general.interfaces.MessagesCallback
import com.pulse.core.general.interfaces.ProgressCallback
import com.pulse.core.network.Resource
import com.pulse.core.network.Resource.*
import com.pulse.mercureService.MercureEventListenerService.Companion.EXTRA_CHAT_ID
import com.pulse.ui.SelectableBottomNavView
import com.pulse.user.model.customer.Customer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_progress.*

class MainActivity : BaseMVVMActivity<MainViewModel>(R.layout.activity_main, MainViewModel::class), ProgressCallback, MessagesCallback {

    private val progressBehavior by lazy { attachBehavior(ProgressViewBehavior(progressRoot)) }
    private val messagesBehavior by lazy { attachBehavior(DialogMessagesBehavior(this)) }

    private val topLevelDestinations = intArrayOf(R.id.nav_home, R.id.nav_catalog, R.id.nav_search, R.id.nav_cart, R.id.nav_profile, R.id.nav_guest_profile)
    private val authDestinations = intArrayOf(R.id.nav_sign_in, R.id.nav_sign_up, R.id.nav_code)
    private val onboardingDestinations = intArrayOf(R.id.nav_on_boarding)
    private val splashDestinations = intArrayOf(R.id.nav_splash)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()

        checkIntentChatId(intent)
    }

    private fun checkIntentChatId(intent: Intent?) {
        intent?.extras?.let {
            val chatId = it.getInt(EXTRA_CHAT_ID, -1)
            if (chatId != -1) {
                observeResult(viewModel.goToChat(chatId)) {
                    onEmmit = { navController.navigate(globalToChat(this)) }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntentChatId(intent)
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

    // TODO maybe need to move to BaseMVVMActivity but need to move behaviors...
    protected fun <T> observeResult(liveData: LiveData<Resource<T>>, block: (ObserveGeneral<T>.() -> Unit)? = null) {
        block?.let {
            ObserveGeneral<T>().apply(block).apply {
                observe(liveData) {
                    when (this) {
                        is Success<T> -> {
                            setInProgress(false)
                            onEmmit(data)
                        }
                        is Progress -> {
                            onProgress?.invoke(isLoading) ?: setInProgress(isLoading)
                        }
                        is Error -> {
                            setInProgress(false)
                            onError?.invoke(exception) ?: showError(exception.resId)
                        }
                    }
                }
            }
        } ?: observe(liveData) {
            when (this) {
                is Success<T> -> setInProgress(false)
                is Progress -> setInProgress(isLoading)
                is Error -> {
                    setInProgress(false)
                    showError(exception.resId)
                }
            }
        }
    }

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
            val isLightStatusBar = destination.isAuthDestination || destination.isOnboardingDestination
            val statusBarColor = when {
                destination.isAuthDestination -> R.color.colorGlobalWhite
                destination.isOnboardingDestination -> R.color.colorGreyLight
                destination.isSplashDestination -> R.color.darkBlue
                else -> R.color.primaryBlue
            }
            setStatusBarColor(statusBarColor)
            setLightStatusBar(isLightStatusBar)

            if (destination.isTopLevelDestination) showNav() else hideNav()
            val isChatForeground = destination.id == R.id.nav_chat
            viewModel.setChatForeground(isChatForeground)
            window.setSoftInputMode(
                if (isChatForeground) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            )
            changeSelection(destination)
        }
    }

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
}