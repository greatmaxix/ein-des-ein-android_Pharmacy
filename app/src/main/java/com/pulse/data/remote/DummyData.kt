package com.pulse.data.remote

import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.components.analyzes.list.model.Analyze
import com.pulse.components.checkout.model.PaymentMethodAdapterModel
import com.pulse.components.payments.model.PaymentMethod
import com.pulse.data.remote.model.order.CustomerOrderData
import com.pulse.model.Location
import com.pulse.model.Picture
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

@Deprecated("Mock data object")
object DummyData {

    val reviewReasons = arrayListOf(
        "Медленные ответы",
        "Хамство",
        "Не компетентность",
        "Не спросили рецепт",
        "Советовали очень дорогое"
    )

    fun getRandomDate(): LocalDateTime {
        val minDay = LocalDate.of(1970, 1, 1).toEpochDay()
        val maxDay = LocalDate.of(2020, 12, 31).toEpochDay()
        val randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay)
        val randomDate = LocalDate.ofEpochDay(randomDay)
        val randomTime = LocalTime.of(Random.nextInt(24), Random.nextInt(60), Random.nextInt(60), Random.nextInt(999999999 + 1))
        return LocalDateTime.of(randomDate, randomTime)
    }

    private fun getRandomIntList(max: Int = 100) = 0..Random.nextInt(max)
    private val analyzeCategoryNames = arrayListOf(
        "Лабараторные исследования",
        "Отделение ЭКО",
        "Рентген",
        "УЗИ",
        "Массаж",
        "Урология",
        "Операции",
        "Гинекология",
        "Хирургия",
        "Оториноларинглогия",
        "Услуги операционно - имационн...",
        "Офтальмология",
        "Терапия",
        "Неврология",
        "Нейрохирургия",
        "Гастроэнтерология",
        "Эндокринология",
        "Эндоскопия",
        "Педиатрия",
        "Женская консультация",
        "Акушерское отделение",
        "МРТ",
        "КТ",
        "Маммография",
        "Процедурный кабинет",
        "Аллергология",
        "Физиотерапия",
        "Программы пакеты",
        "Прочие услуги"
    )
    const val LOREM =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."

    private fun randomLorem() = LOREM.take(Random.nextInt(LOREM.length - 1) + 1)
    private val analyzeCategoryInner2
        get() = getRandomIntList(10).mapIndexed { index, _ -> AnalyzeCategory(index.toLong(), "Category level 2 > $index", "C_O_D_E", randomLorem()) }
    private val analyzeCategoryInner1
        get() = getRandomIntList(10).mapIndexed { index, _ -> AnalyzeCategory(index.toLong(), "Category level 1 > $index", nodes = analyzeCategoryInner2) }
    val analyzeCategories = analyzeCategoryNames.mapIndexed { index, name -> AnalyzeCategory(index.toLong(), name, null, null, analyzeCategoryInner1, true) }
    val privilegeType = arrayListOf(
        "Пенсионер",
        "Инвалид",
        "Один кормилец в семье",
        "Страховой полис",
        "Многодетная семья",
        "Ветеран ВОВ"
    )
    val clinicsList = getRandomIntList(20).mapIndexed { index, _ ->
        Clinic(
            index,
            "+7 (098) 000 02 0$index",
            analyzeCategoryNames.random(),
            randomLorem(),
            Random.nextDouble(10.0),
            Location(Random.nextDouble(), Random.nextDouble(), "Some clinic address"),
            Picture("https://web.synevo.ua/online.new/img/Logo_New.png"),
            Random.nextInt(1000)
        )
    }

    private fun randomUserData() = CustomerOrderData(analyzeCategoryNames.random(), clinicsList.random().phone, null, null)

    val analyzesList = getRandomIntList(5).mapIndexed { index, _ ->
        Analyze(
            index,
            randomUserData(),
            Random.nextInt().toString(),
            analyzeCategoryInner2.random(),
            clinicsList.random(),
            getRandomDate(),
            if (Random.nextBoolean()) Random.nextInt(1000) else null,
            Random.nextInt(1000),
            PaymentMethodAdapterModel(PaymentMethod.CASH, true),
            randomLorem()
        )
    }
}