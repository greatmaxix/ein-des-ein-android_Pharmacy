package com.pharmacy.myapp.cart

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.model.TempProductModel
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.loadGlide
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.toast
import kotlinx.android.synthetic.main.item_cart_product.view.*

class CartAdapter : BaseRecyclerAdapter<TempProductModel, CartAdapter.RecommendedViewHolder>() {

    fun setList(list: MutableList<TempProductModel>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder = RecommendedViewHolder.newInstance(parent)

    class RecommendedViewHolder(view: View) : BaseViewHolder<TempProductModel>(view) {

        override fun bind(item: TempProductModel) {
            itemView.ivProductImageCart.loadGlide(item.imageUrl)

            itemView.tvProductTitleCart.text = item.name
            itemView.tvProductDescriptionCart.text = item.description
            itemView.tvProductIssuerCart.text = item.issuer
            itemView.fabProductPriceCart.text = item.price
            itemView.button2.text = "1"
            itemView.button1.onClick {
                val count = itemView.button2.text.toString().toInt()
                if(count < 2) return@onClick
                itemView.button2.text = (count - 1).toString()
            }
            itemView.button3.onClick {
                itemView.button2.text = (itemView.button2.text.toString().toInt() + 1).toString()
            }
            itemView.recommendedFavoriteCart.onClick { itemView.context.toast("TODO: Favorite") }
        }

        companion object {

            fun newInstance(parent: ViewGroup) = RecommendedViewHolder(parent.inflate(R.layout.item_cart_swipe_delete))
        }
    }
}