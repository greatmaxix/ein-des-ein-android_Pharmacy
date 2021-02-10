package com.pulse.components.checkout.adapter

import android.view.ViewGroup
import com.pulse.components.cart.model.CartProduct
import com.pulse.core.base.adapter.BaseRecyclerAdapter

class CheckoutProductsAdapter(list: MutableList<CartProduct>) : BaseRecyclerAdapter<CartProduct, RecommendedViewHolder>(list) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder = RecommendedViewHolder.newInstance(parent)
}