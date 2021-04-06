package com.pulse.components.analyzes.checkout

import com.pulse.components.analyzes.checkout.repository.AnalyzeCheckoutRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.utils.flow.StateEventFlow
import org.koin.core.component.KoinApiExtension
import java.time.LocalDateTime

@KoinApiExtension
class AnalyzeCheckoutViewModel(private val repository: AnalyzeCheckoutRepository) : BaseViewModel() {

    val customerInfoState = repository.getCustomerInfo()
    val selectedDateTimeState = StateEventFlow<LocalDateTime?>(null)
    val pickDateTimeState = StateEventFlow<LocalDateTime?>(null)

    fun handlePromoCodeResult(code: String) {
        // TODO
    }

    fun pickDateTime() = selectedDateTimeState.postState(selectedDateTimeState.value ?: LocalDateTime.now())

    fun setDateTime(localDateTime: LocalDateTime) = selectedDateTimeState.postState(localDateTime)
}