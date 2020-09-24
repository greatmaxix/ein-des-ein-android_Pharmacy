package com.pharmacy.myapp.cart.adapter

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.pharmacy.myapp.R
import com.pharmacy.myapp.cart.model.CartItem
import com.pharmacy.myapp.cart.model.CartProduct
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.*
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

            ivLogo.loadGlide(cardItem.logo.url) {
                placeholder(R.drawable.ic_drugstore_base)
                RequestOptions.bitmapTransform(CircleCrop())
                transition(DrawableTransitionOptions().crossFade())
            }

            tvTitle.text = cardItem.name
            tvSubTitle.text = cardItem.location.address

            ivArrow.rotation = if (item.second) EXPANDED_DEG else COLLAPSED_DEG
            onClick(item.third)
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
                ivProduct.loadGlide(product.pictures.first().url) {
                    transform(CenterCrop(), RoundedCorners(R.dimen._8sdp.toPixelSize))
                }
            }

            tvTitle.setTextHtml(product.rusName)
            tvSubTitle.setTextHtml(product.releaseForm)

            mbCounter.text = product.cartProductInfo.count.toString()

            fun setNewCounterValue(newValue: Int) {
                mbCounter.text = newValue.toString()
                notifyCounter(newValue)
            }

            mbPlus.onClick { setNewCounterValue(mbCounter.count + 1) }
            mbMinus.onClick {
                val count = mbCounter.count
                if (count == 1) removeClick(product.productId) else setNewCounterValue(count - 1)
            }

            tvManufacture.setTextHtml(stringRes(R.string.manufacture, product.productLocale))
            tvProductPrice.text = stringRes(R.string.price, product.price.formatPrice())

            ivRemove.onClick { removeClick(product.productId) }
        }

        companion object {
            fun newInstance(parent: ViewGroup, removeClick: (Int) -> Unit) = CartItemViewHolder(parent.inflate(R.layout.item_cart_content), removeClick)
        }
    }

    class CartFooterViewHolder(itemView: View, private val notifyCheckout: (CartItem) -> Unit) : CartViewHolder<CartItem>(itemView) {

        override fun bind(item: CartItem) = with(itemView) {
            mbCheckout.onClick { notifyCheckout(item) }
            tvTotalPrice.text = stringRes(R.string.price, item.totalPrice.formatPrice())
            tvNumberProducts.text = stringRes(R.string.countCurtProducts, item.totalCount)
        }

        companion object {
            fun newInstance(parent: ViewGroup, notifyCheckout: (CartItem) -> Unit) = CartFooterViewHolder(parent.inflate(R.layout.item_cart_footer), notifyCheckout)
        }
    }
}