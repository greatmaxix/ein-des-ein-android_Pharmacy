package com.pulse.components.checkout.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.cart.model.CartProduct
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.getString
import com.pulse.core.extensions.inflate
import com.pulse.core.extensions.setProductImage
import com.pulse.core.extensions.setTextHtml
import com.pulse.databinding.ItemCheckoutProductBinding

class RecommendedViewHolder(view: View) : BaseViewHolder<CartProduct>(view) {

    private val binding by viewBinding(ItemCheckoutProductBinding::bind)

    override fun bind(item: CartProduct) = with(binding) {
        ivProductImage.setProductImage(item.pictures)
        mtvTitle.setTextHtml(item.rusName)
        mtvDescription.setTextHtml(item.releaseForm)
        mtvIssuer.text = item.manufacture.producer
        mtvCount.text = getString(R.string.productCount, item.count)
        mtvPrice.text = getString(R.string.price, item.price)
    }

    companion object {

        fun newInstance(parent: ViewGroup) = RecommendedViewHolder(parent.inflate(R.layout.item_checkout_product))
    }
}