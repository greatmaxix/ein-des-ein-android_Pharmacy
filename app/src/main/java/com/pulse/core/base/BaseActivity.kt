package com.pulse.core.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.pulse.R
import com.pulse.core.extensions.lazyNavController
import com.pulse.core.locale.ILocaleManager
import org.koin.android.ext.android.get

abstract class BaseActivity(@LayoutRes layoutResourceId: Int) : AppCompatActivity(layoutResourceId) {

    protected val navController by lazyNavController(R.id.nav_host)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initUI()
    }

    /**
     * Here we may init UI components.
     * This method will be executed after parent [onCreate] method
     */
    protected abstract fun initUI()

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(get<ILocaleManager>().createLocalisedContext(newBase))
}