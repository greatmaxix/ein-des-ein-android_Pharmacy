package com.pharmacy.myapp.core.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.pharmacy.myapp.R
import timber.log.Timber

fun NavController.onNavDestinationSelected(
    @IdRes destId: Int,
    args: Bundle? = null,
    startDestination: Int = graph.startDestination,
    inclusive: Boolean = false,
    navigatorExtras: Navigator.Extras? = null
) = try {
    navigate(destId, args, getNavOptions().setPopUpTo(startDestination, inclusive).build(), navigatorExtras)
    true
} catch (e: IllegalArgumentException) {
    Timber.e("Destination problem: $e")
    false
}

fun NavController.onNavDestinationSelected(directions: NavDirections) = onNavDestinationSelected(directions.actionId, directions.arguments)

fun getNavOptions() = NavOptions.Builder()
    .setEnterAnim(R.anim.nav_enter_anim)
    .setExitAnim(R.anim.nav_exit_anim)
    .setPopEnterAnim(R.anim.nav_enter_pop_anim)
    .setPopExitAnim(R.anim.nav_exit_pop_anim)