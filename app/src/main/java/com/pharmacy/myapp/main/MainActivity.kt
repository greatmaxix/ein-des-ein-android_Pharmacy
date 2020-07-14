package com.pharmacy.myapp.main

import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMActivity
import com.pharmacy.myapp.core.general.behavior.DialogMessagesBehavior
import com.pharmacy.myapp.core.general.behavior.ProgressViewBehavior
import com.pharmacy.myapp.core.general.interfaces.MessagesCallback
import com.pharmacy.myapp.core.general.interfaces.ProgressCallback
import kotlinx.android.synthetic.main.layout_progress.*

class MainActivity : BaseMVVMActivity<MainViewModel>(R.layout.activity_main, MainViewModel::class),
    ProgressCallback, MessagesCallback {

    private val progressBehavior by lazy { attachBehavior(ProgressViewBehavior(progressRoot)) }
    private val messagesBehavior by lazy { attachBehavior(DialogMessagesBehavior(this)) }

    override fun setInProgress(progress: Boolean) = progressBehavior.setInProgress(progress)

    override fun showError(error: String, action: (() -> Unit)?) =
        messagesBehavior.showError(error, action)

    override fun showError(strResId: Int, action: (() -> Unit)?) =
        messagesBehavior.showError(strResId, action)
}