package com.pulse.main.helper

import androidx.navigation.NavDestination
import com.pulse.components.user.model.customer.Customer
import com.pulse.core.utils.flow.StateEventFlow

interface INavigationHelper {

    val onDestinationChangedFlow: StateEventFlow<NavDestination?>

    val onMenuItemChangedFlow: StateEventFlow<Int?>

    val deepLinkGraphDestination: Pair<Int, Int>

    fun onBackPress(superBackPress: () -> Unit)

    fun setBottomNavItems(customer: Customer?)
}