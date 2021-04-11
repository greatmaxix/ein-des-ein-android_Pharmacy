package com.pulse.components.scanner

import androidx.lifecycle.viewModelScope
import com.pulse.components.product.BaseProductViewModel
import com.pulse.components.product.model.ProductLite
import com.pulse.components.scanner.repository.ScannerRepository
import com.pulse.core.utils.flow.StateEventFlow
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ScannerViewModel(private val repository: ScannerRepository) : BaseProductViewModel() {

    val resultState = StateEventFlow<List<ProductLite>>(listOf())
    val descriptionVisibilityState = StateEventFlow(!repository.isQrCodeDescriptionShown())

    fun descriptionViewed() {
        descriptionVisibilityState.postState(false)
        viewModelScope.execute { repository.setDescriptionShown() }
    }

    fun searchQrCode(code: String) = viewModelScope.execute {
        val items = repository.searchQrCode(code).data.items
        if (items.size == 1) {
            getProductInfo(items.first().globalProductId)
        } else {
            resultState.postState(items)
        }
    }
}