package com.pharmacy.checkout.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.R
import com.pharmacy.components.cart.model.CartProduct
import com.pharmacy.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.core.base.adapter.BaseViewHolder
import com.pharmacy.core.extensions.inflate
import com.pharmacy.core.extensions.setProductImage
import com.pharmacy.core.extensions.setTextHtml
import com.pharmacy.core.extensions.stringRes
import kotlinx.android.synthetic.main.item_checkout_product.view.*

class CheckoutProductsAdapter(list: MutableList<CartProduct>) :
    BaseRecyclerAdapter<CartProduct, CheckoutProductsAdapter.RecommendedViewHolder>(list) { // todo rename or move to base

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder = RecommendedViewHolder.newInstance(parent)

    class RecommendedViewHolder(view: View) : BaseViewHolder<CartProduct>(view) {

        override fun bind(item: CartProduct) {
            with(itemView) {
                ivProductImageCheckout.setProductImage(item.pictures)
                tvProductTitleCheckout.setTextHtml(item.rusName)
                tvProductDescriptionCheckout.setTextHtml(item.releaseForm)
                tvProductIssuerCheckout.text = item.manufacture.producer
                tvCountCheckout.text = stringRes(R.string.productCount, item.count)
                fabProductPriceCheckout.text = stringRes(R.string.price, item.price)
            }
        }

        companion object {

            fun newInstance(parent: ViewGroup) = RecommendedViewHolder(parent.inflate(R.layout.item_checkout_product))
        }
    }
}