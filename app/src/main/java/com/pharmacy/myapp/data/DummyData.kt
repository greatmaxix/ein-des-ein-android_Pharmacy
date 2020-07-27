package com.pharmacy.myapp.data

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
}