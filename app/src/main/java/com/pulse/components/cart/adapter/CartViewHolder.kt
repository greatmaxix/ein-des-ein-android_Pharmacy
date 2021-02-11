package com.pulse.components.cart.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.button.MaterialButton
import com.pulse.R
import com.pulse.components.cart.model.CartItem
import com.pulse.components.cart.model.CartProduct
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.databinding.ItemCartContentBinding
import com.pulse.databinding.ItemCartFooterBinding
import com.pulse.databinding.ItemCartHeaderBinding

sealed class CartViewHolder<T>(itemView: View) : BaseViewHolder<T>(itemView) {

    class CartHeaderViewHolder(itemView: View) : CartViewHolder<Triple<CartItem, Boolean, () -> Unit>>(itemView) {

        private val binding by viewBinding(ItemCartHeaderBinding::bind)
        internal val ivArrow = binding.ivArrow

        override fun bind(item: Triple<CartItem, Boolean, () -> Unit>) = with(binding) {
            val cardItem = item.first

            ivLogo.loadGlideDrugstore(cardItem.logo.url)

            mtvTitle.text = cardItem.name
            mtvSubtitle.text = cardItem.location.address

            ivArrow.rotation = if (item.second) EXPANDED_DEG else COLLAPSED_DEG
            root.onClickDebounce(item.third)
        }

        companion object {

            private const val EXPANDED_DEG = 270f
            private const val COLLAPSED_DEG = 90f

            fun newInstance(parent: ViewGroup) = CartHeaderViewHolder(parent.inflate(R.layout.item_cart_header))
        }
    }

    class CartItemViewHolder(itemView: View, private val removeClick: (Int) -> Unit) :
        CartViewHolder<Pair<CartProduct, (Int) -> Unit>>(itemView) {

        private val binding by viewBinding(ItemCartContentBinding::bind)
        private val MaterialButton.count
            get() = binding.mbCounter.text().toInt()

        override fun bind(item: Pair<CartProduct, (Int) -> Unit>) = with(binding) {
            val (product, notifyCounter) = item

            if (product.pictures.isNotEmpty()) {
                ivProduct.loadGlideDrawableByURL(product.pictures.first().url) {
                    transform(CenterCrop(), RoundedCorners(R.dimen._8sdp.toPixelSize))
                }
            }

            mtvTitle.setTextHtml(product.rusName)
            mtvSubtitle.setTextHtml(product.releaseForm)
            mbCounter.text = product.count.toString()

            fun setNewCounterValue(newValue: Int) {
                mbCounter.text = newValue.toString()
                notifyCounter(newValue)
            }

            mbPlus.setOnClickListener { setNewCounterValue(mbCounter.count + 1) }
            mbMinus.setOnClickListener {
                val count = mbCounter.count
                if (count == 1) removeClick(product.productId) else setNewCounterValue(count - 1)
            }

            mtvManufacture.setTextHtml(getString(R.string.manufacture, product.productLocale))
            mtvProductPrice.text = getString(R.string.price, product.price.formatPrice())
            ivRemove.setDebounceOnClickListener { removeClick(product.productId) }
            viewDivider.visibleOrInvisible(!product.needShowDivider)
        }

        companion object {

            fun newInstance(parent: ViewGroup, removeClick: (Int) -> Unit) = CartItemViewHolder(parent.inflate(R.layout.item_cart_content), removeClick)
        }
    }

    class CartFooterViewHolder(itemView: View, private val notifyCheckout: (CartItem) -> Unit) : CartViewHolder<CartItem>(itemView) {

        private val binding by viewBinding(ItemCartFooterBinding::bind)

        override fun bind(item: CartItem) = with(binding) {
            mbCheckout.setDebounceOnClickListener { notifyCheckout(item) }
            mtvTotalPrice.text = getString(R.string.price, item.totalPrice.formatPrice())
            mtvNumberProducts.text = getString(R.string.countCurtProducts, item.totalCount)
        }

        companion object {

            fun newInstance(parent: ViewGroup, notifyCheckout: (CartItem) -> Unit) = CartFooterViewHolder(parent.inflate(R.layout.item_cart_footer), notifyCheckout)
        }
    }
}