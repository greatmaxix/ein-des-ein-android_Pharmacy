package com.pulse.components.region

import androidx.lifecycle.viewModelScope
import com.pulse.components.region.repository.RegionRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.model.region.Region
import com.pulse.model.region.RegionWithHeader
import kotlinx.coroutines.delay
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class RegionViewModel(private val repository: RegionRepository) : BaseViewModel() {

    val regionsState = StateEventFlow<MutableList<RegionWithHeader>>(mutableListOf())
    val regionSavedEvent = SingleShotEvent<Boolean>()

    private var originalList = emptyList<Region>()
    private var itemClicked = false

    init {
        viewModelScope.execute {
            val items = repository.getRegions().data.items
            originalList = items
            regionsState.postState(addHeaders(items.toMutableList()))
        }
    }

    private fun addHeaders(list: MutableList<Region>): MutableList<RegionWithHeader> {
        var omittedList: MutableList<Region> = list
        if (list.size == 1) {
            omittedList = list[0].children.toMutableList()
        }
        val withHeader = mutableListOf<RegionWithHeader>()
        omittedList.forEachIndexed { index, region ->
            if (index == 0 || region.firstCharOfName != omittedList[index - 1].firstCharOfName) {
                withHeader.add(RegionWithHeader(null, region.name.first()))
            }
            withHeader.add(RegionWithHeader(region, 0.toChar()))
        }
        return withHeader
    }

    fun regionSelected(region: Region) {
        val children = region.children
        if (children.isNullOrEmpty()) {
            setRegion(region)
        } else {
            regionsState.postState(addHeaders(children.toMutableList()))
            itemClicked = true
        }
    }

    private fun setRegion(region: Region) = viewModelScope.execute {
        if (repository.getCustomer() == null) {
            repository.saveRegionLocally(region)
        } else {
            val response = repository.updateCustomerRegion(region.id)
                repository.saveCustomerInfoLocally(response.data.item)
        }
        delay(1000) // wait for animation end
        regionSavedEvent.offerEvent(true)
    }

    fun handleBackPress() {
        if (itemClicked) {
            itemClicked = false
            regionsState.postState(addHeaders(originalList.toMutableList()))
            return
        }
        regionSavedEvent.offerEvent(false)
    }
}