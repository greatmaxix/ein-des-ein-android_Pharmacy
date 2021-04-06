package com.pulse.components.user.profile.guest

import com.pulse.components.region.repository.RegionRepository
import com.pulse.core.base.mvvm.BaseViewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class GuestProfileViewModel(repository: RegionRepository) : BaseViewModel() {

    var tempRegionFlow = repository.getTemporaryRegion()
}