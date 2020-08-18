package com.pharmacy.myapp.checkoutMap

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkoutMap.model.TempAvailableDrugstore
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.textColor
import com.pharmacy.myapp.core.extensions.toast
import kotlinx.android.synthetic.main.item_checkout_store.view.*

class AvailableDrugstoresAdapter(list: List<TempAvailableDrugstore>) : BaseRecyclerAdapter<TempAvailableDrugstore, AvailableDrugstoresAdapter.AvailableDrugstoresViewHolder>(list) {

    class AvailableDrugstoresViewHolder(itemView: View) : BaseViewHolder<TempAvailableDrugstore>(itemView) {
        override fun bind(item: TempAvailableDrugstore) {
            with(itemView) {
                tvAvailabilityInDrugstore.textColor(item.availabilityColor())
                tvAvailabilityInDrugstore.text = item.availability
                tvDrugstoreName.text = item.name
                tvDrugstoreAddress.text = item.address
                tvContactInformation.text = item.contactInfo
                tvWorkingHours.text = item.workingHours
                tvPrice.text = item.price
                btnChooseDrugstore.onClick { itemView.context.toast("TODO: Choose") }
                ivDrugstoreLocation.onClick { itemView.context.toast("TODO: Location") }
            }
        }

        companion object {

            fun newInstance(parent: ViewGroup) = AvailableDrugstoresViewHolder(parent.inflate(R.layout.item_checkout_store))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AvailableDrugstoresViewHolder.newInstance(parent)

}