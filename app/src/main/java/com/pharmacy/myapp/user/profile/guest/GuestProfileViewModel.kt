package com.pharmacy.myapp.user.profile.guest

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.region.repository.RegionRepository
import kotlinx.coroutines.Dispatchers

class GuestProfileViewModel(repository: RegionRepository) : BaseViewModel() {

    var tempRegionLiveData = repository.getTemporaryRegion().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

}