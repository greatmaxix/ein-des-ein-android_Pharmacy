package com.pharmacy.myapp.pharmacy.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pharmacy.myapp.pharmacy.model.Pharmacy

object PharmacyDiffUtil : DiffUtil.ItemCallback<Pharmacy>() {

    override fun areItemsTheSame(oldItem: Pharmacy, newItem: Pharmacy) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Pharmacy, newItem: Pharmacy) = oldItem == newItem

}