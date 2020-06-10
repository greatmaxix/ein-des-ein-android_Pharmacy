package com.pharmacy.myapp.core.base

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.animatorMedium
import com.pharmacy.myapp.core.extensions.getCompatColor
import com.pharmacy.myapp.core.extensions.mixColorWith
import com.pharmacy.myapp.core.extensions.setMenu
import com.pharmacy.myapp.core.general.behavior.IBehavior
import com.pharmacy.myapp.core.general.interfaces.ILocaleHelper
import org.koin.android.ext.android.get

abstract class BaseActivity(@LayoutRes layoutResourceId: Int) : AppCompatActivity(layoutResourceId) {

    companion object {
        val ANIM_EXIT = R.anim.nav_exit_anim
        val ANIM_ENTER = R.anim.nav_enter_anim
    }

    var toolbar: Toolbar? = null
        private set

    private val behaviors = mutableListOf<IBehavior?>()

    protected val navController: NavController by lazy {
        try {
            findNavController(R.id.navHost)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("${this::class.java.simpleName} does not use navController\"")
        }
    }

    override fun attachBaseContext(newBase: Context) = super.attachBaseContext(get<ILocaleHelper>().createLocalisedContext(newBase))

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(ANIM_ENTER, ANIM_EXIT)
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(@LayoutRes layoutResId: Int) {
        super.setContentView(layoutResId)
        toolbar = findViewById(R.id.toolbar)
    }

    fun initToolbar(appBarConfiguration: AppBarConfiguration) {
        toolbar?.setupWithNavController(navController, appBarConfiguration)
        toolbar?.setNavigationOnClickListener { navController.navigateUp(appBarConfiguration) }
        navController.addOnDestinationChangedListener { _, _, _ ->
            toolbar?.let {
                if (it.menu.size() > 0) {
                    it.menu.clear()
                }
            }
        }
    }

    fun showBackButton() {
        toolbar?.setupWithNavController(navController)
        toolbar?.setNavigationOnClickListener { navController.navigateUp() }
    }

    fun initMenu(@MenuRes menu: Int, itemClick: Toolbar.OnMenuItemClickListener? = null) {
        toolbar?.setMenu(menu, itemClick ?: Toolbar.OnMenuItemClickListener { onOptionsItemSelected(it) })
    }

    override fun setTitle(title: CharSequence?) {
        toolbar?.title = title
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    protected fun <T : IBehavior> attachBehavior(behavior: T) = behavior.also {
        behaviors.add(it)
    }

    @CallSuper
    override fun onDestroy() {
        behaviors.forEach { it?.detach() }
        behaviors.clear()
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(ANIM_ENTER, ANIM_EXIT)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        overridePendingTransition(ANIM_ENTER, ANIM_EXIT)
    }

    fun setToolbarContentColor(@ColorRes color: Int) {
        val colorInt = getCompatColor(color)
        toolbar?.setTitleTextColor(colorInt)
        toolbar?.navigationIcon?.setColorFilter(colorInt, PorterDuff.Mode.SRC_ATOP)
    }

    fun setToolbarColorInt(@ColorInt colorTo: Int, @ColorInt colorFrom: Int? = null) {
        if (colorFrom != null) {
            animatorMedium {
                val color = colorFrom.mixColorWith(colorTo, it)
                toolbar?.setBackgroundColor(color)
            }
        } else {
            toolbar?.setBackgroundColor(colorTo)
        }
    }

    fun setToolbarColor(@ColorRes colorFrom: Int, @ColorRes colorTo: Int? = null) {
        setToolbarColorInt(getCompatColor(colorFrom), if (colorTo == null) null else getCompatColor(colorTo))
    }
}