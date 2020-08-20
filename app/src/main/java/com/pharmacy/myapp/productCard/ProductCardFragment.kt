package com.pharmacy.myapp.productCard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.setTopRoundCornerBackground
import com.pharmacy.myapp.core.extensions.toast
import com.pharmacy.myapp.data.DummyData
import com.pharmacy.myapp.productCard.adapter.ProductCardImageAdapter
import com.pharmacy.myapp.productCard.adapter.RecommendedAdapter
import kotlinx.android.synthetic.main.fragment_product_card.*
import kotlinx.android.synthetic.main.layout_product_card_additional_info.*
import kotlinx.android.synthetic.main.layout_product_card_image_pager.*
import kotlinx.android.synthetic.main.layout_product_card_main_info.*

class ProductCardFragment(private val viewModel: ProductCardViewModel) : BaseMVVMFragment(R.layout.fragment_product_card) {

    private val imageAdapter = ProductCardImageAdapter()
    private val recommendedAdapter = RecommendedAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        initMenu(R.menu.share, Toolbar.OnMenuItemClickListener {
            if (it.itemId == R.id.menu_share) {
                // TODO share func
                requireContext().toast("TODO: Share")
            }
            true
        })

        initImagePager()
        initRecommended()
        bottomLayout.setTopRoundCornerBackground()

        fillTempData()

        productAnalogContainer.onClick { requireContext().toast("TODO: Analogs") }
        categoryContainer.onClick { requireContext().toast("TODO: Category") }
        instructionContainer.onClick { requireContext().toast("TODO: Instruction") }
        questionsContainer.onClick { requireContext().toast("TODO: Ask pharmacist") }
        seeAllRecommended.onClick { requireContext().toast("TODO: See all recommended") }
        favoriteFab.onClick { requireContext().toast("TODO: Favorite") }
        addToCartBtn.onClick { requireContext().toast("TODO: Add to cart") }
    }

    private fun fillTempData() {
        toolbar?.title = "Нош-па"
        productRecipeLabel.text = "SOME_ITEM_RECIPE"

        productTitle.text = "АЛЛОПУРИНОЛ-ЭГИС, 40 мг"
        productShortDescription.text = "Таблетки шипучие, 24 шт"
        productIssuer.text = "Chinoin Pharmaceutical and Chemical Works Co.  Венгрия"
        productPrice.text = "от 568 ₽ - 985₽"
        productBonus.text = "\uD83E\uDD1150 БОНСУОВ"

        analogTitle.text = "Аналоги и основное действующее вещетсво"
        analogName.text = "Дротаверин"

        categoryIcon.setImageResource(R.drawable.ic_vaccine)
        categoryName.text = "Противогрибковый"

        val inflater = LayoutInflater.from(requireContext())
        DummyData.getTags().forEach {
            tagsGroup.addView(inflater.inflate(R.layout.item_tag_chip, null)
                .apply {
                    (this as Chip).text = it
                })
        }

        descriptionText.text =
            "Таблетки покрытые пленочной оболочкой от светло-серого до темно-серого цвета, капсуловидной формы, сгравировкой \"PRENATAL\"\" с одной стороны и \"FORTE\" с другой стороны, со специфическим запахом"
        seeAllRecommended.text = "Смотреть все (23)"
        addToCartBtn.text = "В корзину от 568 ₽ -985 ₽"
    }

    private fun initImagePager() {
        val items = DummyData.getProductImages()
        productImagePager.adapter = imageAdapter
        imageAdapter.setList(items)
        TabLayoutMediator(productImagePagerIndicator, productImagePager) { _, _ ->
            // no op
        }.attach()
    }

    private fun initRecommended() {
        val items = DummyData.getRecommended()
        recommendedList.setHasFixedSize(true)
        recommendedList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recommendedList.adapter = recommendedAdapter
        recommendedAdapter.setList(items)
    }
}