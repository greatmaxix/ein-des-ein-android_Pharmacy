package com.pulse.components.needHelp

import com.pulse.components.needHelp.repository.NeedHelpRepository
import com.pulse.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class NeedHelpViewModel(private val repository: NeedHelpRepository) : BaseViewModel()