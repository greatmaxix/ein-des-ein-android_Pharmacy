package com.pharmacy.myapp.data

import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.model.TempPaymentMethod
import com.pharmacy.myapp.checkout.model.TempProductModel
import com.pharmacy.myapp.myOrders.model.MyOrder
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

    private fun getMyOrderPreviewImages() = listOf(
        "https://ichef.bbci.co.uk/news/976/cpsprodpb/13672/production/_112947497_2add3259-7faf-45df-9618-dd98fc1e53a6.jpg",
        "https://as2.ftcdn.net/jpg/01/88/71/03/500_F_188710349_3A3hMUoHsRraqzr20m9gGhV8B7oBA6d3.jpg",
        "https://images-na.ssl-images-amazon.com/images/I/71e6Si4GmeL._AC_SX425_.jpg",
        "https://ichef.bbci.co.uk/news/976/cpsprodpb/13672/production/_112947497_2add3259-7faf-45df-9618-dd98fc1e53a6.jpg",
        "https://images-na.ssl-images-amazon.com/images/I/71e6Si4GmeL._AC_SX425_.jpg"
    )

    fun getMyOrders() = mutableListOf(
        MyOrder(12582, "В обработке", "Самовывоз", "21:12 05.06.20", "“Ригла “, ул Горная 23а, Харьков", getMyOrderPreviewImages(), "7879 ₽"),
        MyOrder(12582, "В обработке", "Самовывоз", "21:12 05.06.20", "“Ригла “, ул Горная 23а, Харьков", listOf(
                "https://as2.ftcdn.net/jpg/01/88/71/03/500_F_188710349_3A3hMUoHsRraqzr20m9gGhV8B7oBA6d3.jpg",
                "https://images-na.ssl-images-amazon.com/images/I/71e6Si4GmeL._AC_SX425_.jpg",
                "https://ichef.bbci.co.uk/news/976/cpsprodpb/13672/production/_112947497_2add3259-7faf-45df-9618-dd98fc1e53a6.jpg",
                "https://images-na.ssl-images-amazon.com/images/I/71e6Si4GmeL._AC_SX425_.jpg"
            ), "7879 ₽"),
        MyOrder(12583, "Выполнен", "Доставка", "21:12 05.06.20", "\uD83C\uDFE0 ул. Горная 23а, Харьков \uD83D\uDEAAдом 4г • кв. 56", listOf(
                "https://ichef.bbci.co.uk/news/976/cpsprodpb/13672/production/_112947497_2add3259-7faf-45df-9618-dd98fc1e53a6.jpg",
                "https://as2.ftcdn.net/jpg/01/88/71/03/500_F_188710349_3A3hMUoHsRraqzr20m9gGhV8B7oBA6d3.jpg",
                "https://images-na.ssl-images-amazon.com/images/I/71e6Si4GmeL._AC_SX425_.jpg"
            ), "6568 ₽")
        ,MyOrder(12582, "В обработке", "Самовывоз", "21:12 05.06.20", "“Ригла “, ул Горная 23а, Харьков", getMyOrderPreviewImages(), "7879 ₽"),
        MyOrder(12582, "В обработке", "Самовывоз", "21:12 05.06.20", "“Ригла “, ул Горная 23а, Харьков", getMyOrderPreviewImages(), "7879 ₽"),
        MyOrder(12582, "В обработке", "Самовывоз", "21:12 05.06.20", "“Ригла “, ул Горная 23а, Харьков", getMyOrderPreviewImages(), "7879 ₽"),
        MyOrder(12582, "В обработке", "Самовывоз", "21:12 05.06.20", "“Ригла “, ул Горная 23а, Харьков", getMyOrderPreviewImages(), "7879 ₽"),
        MyOrder(12582, "В обработке", "Самовывоз", "21:12 05.06.20", "“Ригла “, ул Горная 23а, Харьков", getMyOrderPreviewImages(), "7879 ₽"))

}