package com.pulse.components.needHelp.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.pulse.R

enum class Help(
    @DrawableRes val iconRes: Int,
    @StringRes val title: Int,
    @StringRes val content: Int
) {

    HOW_TO_BUY(R.drawable.ic_shopping_cart, R.string.how_to_buy_title, R.string.how_to_buy_content),
    REFUND(R.drawable.ic_double_arrow, R.string.refund_title, R.string.refund_content),
    RECIPES(R.drawable.ic_recipes, R.string.recipes_title, R.string.recipes_content),
    PAY_AND_BONUS(R.drawable.ic_credit_card, R.string.pay_and_bonus_title, R.string.pay_and_bonus_content),
    DELIVERY(R.drawable.ic_delivery, R.string.delivery_title, R.string.delivery_content),
    CONTACT_US(R.drawable.ic_mail, R.string.contact_us_title, R.string.contact_us_content)
}