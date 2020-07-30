package com.pharmacy.myapp.checkout.adapter

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.model.TempProductModel
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.toast
import kotlinx.android.synthetic.main.item_order_product.view.*

class OrderProductsAdapter : BaseRecyclerAdapter<TempProductModel, OrderProductsAdapter.RecommendedViewHolder>() {

    fun setList(list: MutableList<TempProductModel>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder = RecommendedViewHolder.newInstance(parent)

    class RecommendedViewHolder(view: View) : BaseViewHolder<TempProductModel>(view) {

        override fun bind(item: TempProductModel) {
            Glide.with(itemView)
                .load(item.imageUrl)
                .into(itemView.ivProductImageCheckout)

            itemView.tvProductTitleCheckout.text = item.name
            itemView.tvProductDescriptionCheckout.text = item.description
            itemView.tvProductIssuerCheckout.text = item.issuer
            itemView.fabAddToCartCheckout.onClick { itemView.context.toast("TODO: Add to cart") }
            itemView.fabProductPriceCheckout.text = item.price
        }

        companion object {

            fun newInstance(parent: ViewGroup) = RecommendedViewHolder(parent.inflate(R.layout.item_order_product))
        }
    }
}