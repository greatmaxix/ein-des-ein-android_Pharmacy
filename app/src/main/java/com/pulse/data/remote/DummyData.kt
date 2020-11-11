package com.pulse.data.remote

import com.pulse.R
import com.pulse.checkout.model.TempPaymentMethod
import com.pulse.components.needHelp.model.HelpItem
import com.pulse.product.model.TempRecommendedModel

@Deprecated("Mock data object")
object DummyData {

    fun getRecommended() = arrayListOf(
        TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₸"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₸"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₸"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₸"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₸"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₸"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₸"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₸"
        )
    )

    val paymentMethod = arrayListOf(
        TempPaymentMethod("Банковская карта"/* • 9876"*/, R.drawable.ic_mastercard),
        TempPaymentMethod("Kaspi bank", R.drawable.ic_kaspi_bank),
        TempPaymentMethod("Наличными", R.drawable.ic_cash, true)
    )

    val pharmacyResponses = arrayListOf(
        "Чем еще могу быть полезен?",
        "На что у вас есть аллергия?",
        "Какие препараты вы сейчас принимаете?",
        "Есть ли у вас хронические заболевания?",
        "Беременны ли вы или не в процессе грудного вскармливания?"
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