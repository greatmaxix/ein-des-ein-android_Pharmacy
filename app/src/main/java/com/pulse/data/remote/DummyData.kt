package com.pulse.data.remote

import com.pulse.R
import com.pulse.components.analyzes.category.model.AnalyzeCategory
import com.pulse.components.checkout.model.TempPaymentMethod
import com.pulse.components.needHelp.model.HelpItem
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

    fun getRandomIntList(max: Int = 100) = 0..Random.nextInt(max)
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
    private val analyzeCategoryInner2
        get() = getRandomIntList(10).mapIndexed { index, _ -> AnalyzeCategory(index.toLong(), "Category level 2 > $index", "C_O_D_E") }
    private val analyzeCategoryInner1
        get() = getRandomIntList(10).mapIndexed { index, _ -> AnalyzeCategory(index.toLong(), "Category level 1 > $index", nodes = analyzeCategoryInner2) }
    val analyzeCategories = analyzeCategoryNames.mapIndexed { index, name -> AnalyzeCategory(index.toLong(), name, null, analyzeCategoryInner1, true) }
    val paymentMethod = arrayListOf(
        TempPaymentMethod("Банковская карта"/* • 9876"*/, R.drawable.ic_mastercard),
        TempPaymentMethod("Kaspi bank", R.drawable.ic_kaspi_bank),
        TempPaymentMethod("Наличными", R.drawable.ic_cash, true)
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