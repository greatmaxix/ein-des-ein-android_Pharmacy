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
import kotlinx.android.synthetic.main.item_checkout_store.view.*

class AvailableDrugstoresAdapter(list: List<TempAvailableDrugstore>) : BaseRecyclerAdapter<TempAvailableDrugstore, AvailableDrugstoresAdapter.AvailableDrugstoresViewHolder>(list) {

    class AvailableDrugstoresViewHolder(itemView: View) : BaseViewHolder<TempAvailableDrugstore>(itemView) {
        override fun bind(item: TempAvailableDrugstore) {
            val availabilityColor = when (item.availability) {
                "Все в наличии" -> R.color.colorAccent
                "3/4 в наличии" -> R.color.orange
                else -> R.color.primaryBlue
            }
            itemView.tvAvailabilityInDrugstore.textColor(availabilityColor)
            itemView.tvAvailabilityInDrugstore.text = item.availability
            itemView.tvDrugstoreName.text = item.name
            itemView.tvDrugstoreAddress.text = item.address
            itemView.tvContactInformation.text = item.contactInfo
            itemView.tvWorkingHours.text = item.workingHours
            itemView.tvPrice.text = item.price
            itemView.btnChooseDrugstore.onClick { }
            itemView.ivDrugstoreLocation.onClick { }
        }

        companion object {

            fun newInstance(parent: ViewGroup) = AvailableDrugstoresViewHolder(parent.inflate(R.layout.item_checkout_store))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AvailableDrugstoresViewHolder.newInstance(parent)

}