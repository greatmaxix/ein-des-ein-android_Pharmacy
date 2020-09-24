package com.pharmacy.myapp.checkout.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.cart.model.CartProduct
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.setProductImage
import com.pharmacy.myapp.core.extensions.setTextHtml
import com.pharmacy.myapp.core.extensions.stringRes
import kotlinx.android.synthetic.main.item_checkout_product.view.*

class CheckoutProductsAdapter(list: MutableList<CartProduct>) : BaseRecyclerAdapter<CartProduct, CheckoutProductsAdapter.RecommendedViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder = RecommendedViewHolder.newInstance(parent)

    class RecommendedViewHolder(view: View) : BaseViewHolder<CartProduct>(view) {

        override fun bind(item: CartProduct) {
            with(itemView) {
                ivProductImageCheckout.setProductImage(item.pictures)
                tvProductTitleCheckout.setTextHtml(item.rusName)
                tvProductDescriptionCheckout.setTextHtml(item.releaseForm)
                tvProductIssuerCheckout.text = item.manufacture.producer
                tvCountCheckout.text = String.format("x %d", item.cartProductInfo.count)
                fabProductPriceCheckout.text = stringRes(R.string.price, item.price)
            }
        }

        companion object {

            fun newInstance(parent: ViewGroup) = RecommendedViewHolder(parent.inflate(R.layout.item_checkout_product))
        }
    }
}