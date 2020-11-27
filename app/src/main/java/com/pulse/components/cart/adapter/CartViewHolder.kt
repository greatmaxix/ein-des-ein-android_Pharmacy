package com.pulse.components.cart.adapter

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.button.MaterialButton
import com.pulse.R
import com.pulse.components.cart.model.CartItem
import com.pulse.components.cart.model.CartProduct
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import kotlinx.android.synthetic.main.item_cart_content.view.*
import kotlinx.android.synthetic.main.item_cart_footer.view.*
import kotlinx.android.synthetic.main.item_cart_header.view.*
import kotlinx.android.synthetic.main.item_cart_header.view.tvSubTitle
import kotlinx.android.synthetic.main.item_cart_header.view.tvTitle

sealed class CartViewHolder<T>(itemView: View) : BaseViewHolder<T>(itemView) {

    class CartHeaderViewHolder(itemView: View) : CartViewHolder<Triple<CartItem, Boolean, () -> Unit>>(itemView) {

        internal val ivArrow = itemView.ivArrow

        override fun bind(item: Triple<CartItem, Boolean, () -> Unit>) = with(itemView) {
            val cardItem = item.first

            ivLogo.loadGlideDrugstore(cardItem.logo.url)

            tvTitle.text = cardItem.name
            tvSubTitle.text = cardItem.location.address

            ivArrow.rotation = if (item.second) EXPANDED_DEG else COLLAPSED_DEG
            onClickDebounce(item.third)
        }

        companion object {
            fun newInstance(parent: ViewGroup) = CartHeaderViewHolder(parent.inflate(R.layout.item_cart_header))

            private const val EXPANDED_DEG = 270f
            private const val COLLAPSED_DEG = 90f
        }
    }

    class CartItemViewHolder(itemView: View, private val removeClick: (Int) -> Unit) :
        CartViewHolder<Pair<CartProduct, (Int) -> Unit>>(itemView) {

        private val MaterialButton.count
            get() = mbCounter.text().toInt()

        override fun bind(item: Pair<CartProduct, (Int) -> Unit>) = with(itemView) {
            val (product, notifyCounter) = item

            if (product.pictures.isNotEmpty()) {
                ivProduct.loadGlideDrawableByURL(product.pictures.first().url) {
                    transform(CenterCrop(), RoundedCorners(R.dimen._8sdp.toPixelSize))
                }
            }

            tvTitle.setTextHtml(product.rusName)
            tvSubTitle.setTextHtml(product.releaseForm)

            mbCounter.text = product.count.toString()

            fun setNewCounterValue(newValue: Int) {
                mbCounter.text = newValue.toString()
                notifyCounter(newValue)
            }

            mbPlus.setDebounceOnClickListener { setNewCounterValue(mbCounter.count + 1) }
            mbMinus.setDebounceOnClickListener {
                val count = mbCounter.count
                if (count == 1) removeClick(product.productId) else setNewCounterValue(count - 1)
            }

            tvManufacture.setTextHtml(getString(R.string.manufacture, product.productLocale))
            tvProductPrice.text = getString(R.string.price, product.price.formatPrice())

            ivRemove.setDebounceOnClickListener { removeClick(product.productId) }
            cartDivider.visibleOrInvisible(!product.needShowDivider)
        }

        companion object {
            fun newInstance(parent: ViewGroup, removeClick: (Int) -> Unit) = CartItemViewHolder(parent.inflate(R.layout.item_cart_content), removeClick)
        }
    }

    class CartFooterViewHolder(itemView: View, private val notifyCheckout: (CartItem) -> Unit) : CartViewHolder<CartItem>(itemView) {

        override fun bind(item: CartItem) = with(itemView) {
            mbCheckout.setDebounceOnClickListener { notifyCheckout(item) }
            tvTotalPrice.text = getString(R.string.price, item.totalPrice.formatPrice())
            tvNumberProducts.text = getString(R.string.countCurtProducts, item.totalCount)
        }

        companion object {
            fun newInstance(parent: ViewGroup, notifyCheckout: (CartItem) -> Unit) = CartFooterViewHolder(parent.inflate(R.layout.item_cart_footer), notifyCheckout)
        }
    }
}