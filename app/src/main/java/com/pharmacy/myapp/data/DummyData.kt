package com.pharmacy.myapp.data

import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.model.TempPaymentMethod
import com.pharmacy.myapp.checkout.model.TempProductModel
import com.pharmacy.myapp.productCard.model.TempRecommendedModel
import com.pharmacy.myapp.productCard.model.TempReleaseFormModel

@Deprecated("Mock data object")
object DummyData {

    fun getTags() = arrayListOf(
        "Спазмы",
        "Головная боль",
        "Болит живот",
        "Нош-па",
        "Гастрит",
        "Язва",
        "Дисменорея"
    )

    fun getProductImages() = arrayListOf(
        "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
        "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
        "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
        "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
        "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
        "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
        "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
        "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png"
    )

    fun getReleaseForms() = arrayListOf(
        TempReleaseFormModel("Порошок д/пригот", "от 568 ₽"),
        TempReleaseFormModel("Порошок", "от 568 ₽"),
        TempReleaseFormModel("Порошок", "от 568 ₽"),
        TempReleaseFormModel("Порошок", "от 568 ₽"),
        TempReleaseFormModel("Порошок", "от 568 ₽")
    )

    fun getRecommended() = arrayListOf(
        TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₽"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₽"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₽"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₽"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₽"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₽"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₽"
        ), TempRecommendedModel(
            "Рецепт",
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "от 568 ₽"
        )
    )

    fun getOrderProducts() = arrayListOf(
        TempProductModel(
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "Баер, Италия",
            "568 ₽"
        ), TempProductModel(
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "Баер, Италия",
            "568 ₽"
        ), TempProductModel(
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "Баер, Италия",
            "568 ₽"
        ), TempProductModel(
            "https://s3.eu-west-1.amazonaws.com/i.apteka24.ua/products/1d1909e2-b7ff-11ea-96c2-0635d0043582-medium.png",
            "Название продукта",
            "Таблетки шипучие, 24 шт",
            "Баер, Италия",
            "568 ₽"
        )
    )

    val paymentMethod = arrayListOf(
        TempPaymentMethod("Google Pay", R.drawable.ic_google_pay),
        TempPaymentMethod("Apple Pay", R.drawable.ic_apple_pay),
        TempPaymentMethod("Credit card • 9876", R.drawable.ic_mastercard),
        TempPaymentMethod("Kaspi bank", R.drawable.ic_kaspi_bank),
        TempPaymentMethod("Наличными", R.drawable.ic_cash)
    )

    val pharmacyResponses = arrayListOf(
        "Чем еще могу быть полезен?",
        "На что у вас есть аллергия?",
        "Какие препараты вы сейчас принимаете?",
        "Есть ли у вас хронические заболевания?",
        "Беременны ли вы или не в процессе грудного вскармливания?"
    )

    fun getReviewReasons() = arrayListOf(
        "Медленные ответы",
        "Хамство",
        "Не компетентность",
        "Не спросили рецепт",
        "Советовали очень дорогое"
    )
}