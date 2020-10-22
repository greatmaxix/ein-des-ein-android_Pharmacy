package com.pulse.user.profile.guest

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.region.repository.RegionRepository
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class GuestProfileViewModel(repository: RegionRepository) : BaseViewModel() {

    var tempRegionLiveData = repository.getTemporaryRegion().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

}