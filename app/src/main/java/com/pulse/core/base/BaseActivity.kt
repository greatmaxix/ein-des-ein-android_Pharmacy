package com.pulse.core.base

import android.content.Context
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.pulse.R
import com.pulse.core.extensions.lazyNavController
import com.pulse.core.general.behavior.IBehavior
import com.pulse.core.locale.ILocaleManager
import org.koin.android.ext.android.get

abstract class BaseActivity(@LayoutRes layoutResourceId: Int) : AppCompatActivity(layoutResourceId) {

    private val behaviors = mutableListOf<IBehavior>()

    protected val navController by lazyNavController(R.id.nav_host)

    protected fun <T : IBehavior> attachBehavior(behavior: T) = behavior.also(behaviors::add)

    @CallSuper
    override fun onDestroy() {
        behaviors.forEach { it.detach() }
        behaviors.clear()
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(get<ILocaleManager>().createLocalisedContext(newBase))
}