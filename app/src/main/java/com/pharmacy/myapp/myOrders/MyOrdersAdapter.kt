package com.pharmacy.myapp.myOrders

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.getCompatColor
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.loadGlide
import com.pharmacy.myapp.core.extensions.visibleOrGone
import com.pharmacy.myapp.myOrders.model.MyOrder
import kotlinx.android.synthetic.main.layout_my_order_item.view.*

class MyOrdersAdapter : BaseRecyclerAdapter<MyOrder, MyOrdersAdapter.MyOrderViewHolder>() {

    fun setList(list: MutableList<MyOrder>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderViewHolder = MyOrderViewHolder.newInstance(parent)

    class MyOrderViewHolder(view: View) : BaseViewHolder<MyOrder>(view) {

        private val options: RequestBuilder<Drawable>.() -> Unit = {
            transition(DrawableTransitionOptions.withCrossFade())
            override(300)
            transform(MultiTransformation(CenterCrop(), RoundedCorners(R.dimen._6sdp.toPixelSize)))
        }

        override fun bind(item: MyOrder) {
            with(itemView) {
                val isStateInProgressColor = if (item.status == "В обработке") R.color.green else R.color.primaryBlue
                stateMyOrder.backgroundTintList = ColorStateList.valueOf(context.getCompatColor(isStateInProgressColor))

                stateMyOrder.text = item.status
                deliveryTypeMyOrder.text = item.deliveryType
                idMyOrder.text = item.id.toString()
                dateTimeMyOrder.text = item.dateTime
                addressMyOrder.text = item.address
                priceMyOrder.text = item.price
                val isPickup = item.deliveryType == "Самовывоз"
                val deliveryIcon = if (isPickup) R.drawable.ic_shopping_bag else R.drawable.ic_delivery
                val deliveryColor = if (isPickup) R.color.deliveryPickupMyOrder else R.color.deliveryAddressMyOrder
                ivDeliveryTypeMyOrder.setImageResource(deliveryIcon)
                deliveryTypeContainer.setCardBackgroundColor(context.getCompatColor(deliveryColor))
                item.images?.let {list ->
                    list[0].let { firstItemPreview.loadGlide(it, options) }
                    list[1].let { secondItemPreview.loadGlide(it, options) }
                    list[2].let { thirdItemPreview.loadGlide(it, options) }
                    restImagesCount.visibleOrGone((item.images.size) > 3)
                    restImagesCount.text = getRestImagesCount(list)
                }
            }
        }

        private fun getRestImagesCount(item: List<String>) = "+" + (item.size - 3).toString()

        companion object {

            fun newInstance(parent: ViewGroup) = MyOrderViewHolder(parent.inflate(R.layout.layout_my_order_item))
        }
    }
}