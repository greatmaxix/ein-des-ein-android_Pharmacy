package com.pharmacy.myapp.myOrders.adapter

import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.model.order.Order
import kotlinx.android.synthetic.main.layout_my_order_item.view.*

class MyOrdersAdapter : PagingDataAdapter<Order, MyOrdersAdapter.MyOrderViewHolder>(OrderDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyOrderViewHolder.newInstance(parent)

    override fun onBindViewHolder(holder: MyOrderViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    class MyOrderViewHolder(view: View) : BaseViewHolder<Order>(view) {

        override fun bind(item: Order) = with(itemView) {
            val isPickup = item.deliveryInfo.deliveryType?.isPickup.falseIfNull()

            stateMyOrder.backgroundTintList = ColorStateList.valueOf(colorCompat(item.orderStatus.statusColor))
            stateMyOrder.text = stringRes(item.orderStatus.getStatusRes)
            item.deliveryInfo.deliveryType?.backgroundColor?.let {
                deliveryTypeContainer.setCardBackgroundColor(colorCompat(it))
            }
            item.deliveryInfo.deliveryType?.textRes?.let { deliveryTypeMyOrder.text = stringRes(it) }
            item.deliveryInfo.deliveryType?.icon?.let(ivDeliveryTypeMyOrder::setImageResource)
            idMyOrder.text = stringRes(R.string.orderNumber, item.id)

            if (isPickup) {
                addressMyOrder.text = item.pharmacy.location.address
            } else {
                val cityAndStreet = item.deliveryInfo.addressOrderData?.streetAndCity
                val house = item.deliveryInfo.addressOrderData?.houseAndApartment
                addressMyOrder.text = stringRes(R.string.orderAddressDelivery, cityAndStreet, house)
            }

            tvProductCount.visibleOrGone(item.isShowProductCount)
            tvProductCount.text = stringRes(item.productCountString, item.productCount)
            imagesContainerMyOrder.visibleOrGone(!item.isShowProductCount)
            with(item.pharmacyProductOrderDataList) {
                getOrNull(0)?.let(firstItemPreview::loadGlideOrder) ?: run { firstItemPreview.gone() }
                getOrNull(1)?.let(secondItemPreview::loadGlideOrder) ?: run { secondItemPreview.gone() }
                getOrNull(2)?.let(thirdItemPreview::loadGlideOrder) ?: run { thirdItemPreview.gone() }
            }
            restImagesCount.visibleOrGone(item.isProductCountVisible)
            restImagesCount.text = item.getRestImagesCount
            priceMyOrder.text = stringRes(R.string.orderCost, item.pharmacyProductsTotalPrice.formatPrice())
        }

        companion object {

            fun newInstance(parent: ViewGroup) = MyOrderViewHolder(parent.inflate(R.layout.layout_my_order_item))
        }
    }
}