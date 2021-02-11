package com.pulse.data.remote

import com.pulse.R
import com.pulse.components.checkout.model.TempPaymentMethod
import com.pulse.components.needHelp.model.HelpItem

@Deprecated("Mock data object")
object DummyData {

    val paymentMethod = arrayListOf(
        TempPaymentMethod("Банковская карта"/* • 9876"*/, R.drawable.ic_mastercard),
        TempPaymentMethod("Kaspi bank", R.drawable.ic_kaspi_bank),
        TempPaymentMethod("Наличными", R.drawable.ic_cash, true)
    )

    val reviewReasons = arrayListOf(
        "Медленные ответы",
        "Хамство",
        "Не компетентность",
        "Не спросили рецепт",
        "Советовали очень дорогое"
    )

    val help = arrayListOf(
        HelpItem(
            R.drawable.ic_shield,
            "Кассовая дисциплина",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Amet rutrum vel non volutpat. Sagittis aliquam mattis tortorLorem ipsum dolor sit amet, consectetur adipiscing elit. Amet rutrum vel non volutpat. Sagittis aliquam mattis tortor"
        ),
        HelpItem(
            R.drawable.ic_locked,
            "Возврат",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Amet rutrum vel non volutpat. Sagittis aliquam mattis tortorLorem ipsum dolor sit amet, consectetur adipiscing elit. Amet rutrum vel non volutpat. Sagittis aliquam mattis tortor"
        ),
        HelpItem(
            R.drawable.ic_locked,
            "Возврат",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Amet rutrum vel non volutpat. Sagittis aliquam mattis tortorLorem ipsum dolor sit amet, consectetur adipiscing elit. Amet rutrum vel non volutpat. Sagittis aliquam mattis tortor"
        ),
        HelpItem(
            R.drawable.ic_dollar_sign,
            "Рецептурные товары",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Amet rutrum vel non volutpat. Sagittis aliquam mattis tortorLorem ipsum dolor sit amet, consectetur adipiscing elit. Amet rutrum vel non volutpat. Sagittis aliquam mattis tortor"
        )
    )
}