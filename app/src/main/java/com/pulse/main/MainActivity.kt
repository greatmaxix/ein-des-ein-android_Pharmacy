package com.pulse.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkBuilder
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.mercureService.MercureEventListenerService.Companion.EXTRA_CHAT_ID
import com.pulse.core.base.mvvm.BaseMVVMActivity
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.onNavDestinationSelected
import com.pulse.core.extensions.setTopRoundCornerBackground
import com.pulse.core.locale.ILocaleManager
import com.pulse.databinding.ActivityMainBinding
import com.pulse.main.helper.INavigationHelper
import com.pulse.main.helper.NavigationHelper
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import org.koin.android.ext.android.inject

class MainActivity : BaseMVVMActivity<MainViewModel>(R.layout.activity_main, MainViewModel::class) {

    private val binding by viewBinding(ActivityMainBinding::bind, R.id.container)
    private val localeManager by inject<ILocaleManager>()
    private val navigationHelper: INavigationHelper by lazy { NavigationHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(ANIM_ENTER, ANIM_EXIT)
        super.onCreate(savedInstanceState)
    }

    override fun initUI() {
        binding.bottomNavigation.setTopRoundCornerBackground()
        navigationHelper.setBottomNavItems(viewModel.getCurrentCustomer())
        checkIntentChatId(intent)
    }

    private fun checkIntentChatId(intent: Intent?) {
        intent?.extras?.let {
            val chatId = it.getInt(EXTRA_CHAT_ID, -1)
            if (chatId != -1) viewModel.goToChat(chatId)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntentChatId(intent)
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(localeManager.createLocalisedContext(newBase))

    override fun finish() {
        overridePendingTransition(ANIM_ENTER, ANIM_EXIT)
        super.finish()
    }

    override fun startActivity(intent: Intent?) {
        overridePendingTransition(ANIM_ENTER, ANIM_EXIT)
        super.startActivity(intent)
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(navigationHelper.onDestinationChangedFlow) {
            it?.let { viewModel.setChatForeground(it.id == R.id.nav_chat) }
        }
        observe(navigationHelper.onMenuItemChangedFlow) {
            it?.let { viewModel.navSelected(it) }
        }
        observe(localeManager.appLocaleFlow
            .distinctUntilChanged()
            .drop(1)
            .map { navigationHelper.deepLinkGraphDestination }
        ) { notifyContextChanged(it.first, it.second) }
        observe(viewModel.customerInfoFlow, navigationHelper::setBottomNavItems)
        observe(viewModel.directionState) { navController.onNavDestinationSelected(it) }
    }

    private fun notifyContextChanged(@NavigationRes navGraphId: Int, @IdRes destId: Int) {
        NavDeepLinkBuilder(this)
            .setGraph(navGraphId)
            .setDestination(destId)
            .createTaskStackBuilder()
            .startActivities()
    }

    override fun onBackPressed() {
        navigationHelper.onBackPress { super.onBackPressed() }
    }

    companion object {

        private const val ANIM_EXIT = R.anim.nav_exit_anim
        private const val ANIM_ENTER = R.anim.nav_enter_anim
    }
}