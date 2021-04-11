package com.pulse.components.analyzes.clinic.tabs

import androidx.annotation.LayoutRes
import com.pulse.core.base.mvvm.BaseMVVMFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
abstract class BaseClinicTabFragment(@LayoutRes private val layoutResourceId: Int) : BaseMVVMFragment<ClinicTabsViewModel>(layoutResourceId, ClinicTabsViewModel::class) {

    override val viewModel: ClinicTabsViewModel by lazy { requireParentFragment().getViewModel() }
}