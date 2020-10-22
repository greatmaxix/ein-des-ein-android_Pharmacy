package com.pulse.checkout.adapter

import android.view.View
import android.view.ViewGroup
import com.pulse.R
import com.pulse.components.cart.model.CartProduct
import com.pulse.core.base.adapter.BaseRecyclerAdapter
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setProductImage
import com.pulse.core.extensions.setTextHtml
import com.pulse.core.extensions.stringRes
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