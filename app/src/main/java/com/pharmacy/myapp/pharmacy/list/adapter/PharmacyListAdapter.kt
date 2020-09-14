package com.pharmacy.myapp.pharmacy.list.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pharmacy.myapp.pharmacy.model.Pharmacy

class PharmacyListAdapter(private val click: (Pharmacy) -> Unit) : PagingDataAdapter<Pharmacy, PharmacyListViewHolder>(PharmacyDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PharmacyListViewHolder.newInstance(parent, click)

    override fun onBindViewHolder(holder: PharmacyListViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

}