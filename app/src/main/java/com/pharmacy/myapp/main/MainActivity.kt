package com.pharmacy.myapp.main

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupWithNavController
import com.pharmacy.myapp.MainGraphDirections.Companion.globalToChat
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMActivity
import com.pharmacy.myapp.core.dsl.ObserveGeneral
import com.pharmacy.myapp.core.extensions.hideNav
import com.pharmacy.myapp.core.extensions.onNavDestinationSelected
import com.pharmacy.myapp.core.extensions.setTopRoundCornerBackground
import com.pharmacy.myapp.core.extensions.showNav
import com.pharmacy.myapp.core.general.behavior.DialogMessagesBehavior
import com.pharmacy.myapp.core.general.behavior.ProgressViewBehavior
import com.pharmacy.myapp.core.general.interfaces.MessagesCallback
import com.pharmacy.myapp.core.general.interfaces.ProgressCallback
import com.pharmacy.myapp.core.network.Resource
import com.pharmacy.myapp.data.remote.model.chat.ChatItem
import com.pharmacy.myapp.mercureService.MercureEventListenerService.Companion.EXTRA_CHAT_ID
import com.pharmacy.myapp.ui.SelectableBottomNavView
import com.pharmacy.myapp.user.model.customer.Customer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_progress.*

class MainActivity : BaseMVVMActivity<MainViewModel>(R.layout.activity_main, MainViewModel::class), ProgressCallback, MessagesCallback {

    private val progressBehavior by lazy { attachBehavior(ProgressViewBehavior(progressRoot)) }
    private val messagesBehavior by lazy { attachBehavior(DialogMessagesBehavior(this)) }

    private val topLevelDestinations = intArrayOf(R.id.nav_home, R.id.nav_catalog, R.id.nav_search, R.id.nav_cart, R.id.nav_profile, R.id.nav_guest_profile)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()

        checkIntentChatId(intent)
    }

    private fun checkIntentChatId(intent: Intent?) {
        intent?.extras?.let {
            val chatId = it.getInt(EXTRA_CHAT_ID, -1)
            if (chatId != -1) {
                observeResult<ChatItem> {
                    liveData = viewModel.goToChat(chatId)
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
    protected fun <T> observeResult(block: ObserveGeneral<T>.() -> Unit) {
        ObserveGeneral<T>().apply(block).apply {
            observe(liveData) {
                when (this) {
                    is Resource.Success<T> -> {
                        setInProgress(false)
                        onEmmit(data)
                    }
                    is Resource.Progress -> {
                        onProgress?.invoke(isLoading) ?: setInProgress(isLoading)
                    }
                    is Resource.Error -> {
                        setInProgress(false)
                        onError?.invoke(exception) ?: showError(exception.resId)
                    }
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

    private val NavDestination.isTopLevelDestination
        get() = topLevelDestinations.contains(id)

    private val NavDestination.isTopDestinationAndHome
        get() = isTopLevelDestination && id == R.id.nav_home

    private val NavDestination.isCategories
        get() = id == R.id.nav_catalog
}