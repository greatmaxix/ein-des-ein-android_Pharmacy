package com.pharmacy.myapp.productCard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.productCard.model.TempReleaseFormModel
import kotlinx.android.synthetic.main.item_release_form.view.*

class ReleaseFormAdapter(items: List<TempReleaseFormModel>) :
    BaseRecyclerAdapter<TempReleaseFormModel, ReleaseFormAdapter.ReleaseFormViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseFormViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_release_form, parent, false)
        return ReleaseFormViewHolder(view)
    }

    class ReleaseFormViewHolder(view: View) : BaseViewHolder<TempReleaseFormModel>(view) {

        override fun bind(item: TempReleaseFormModel) {
            itemView.nameTv.text = item.name
            itemView.priceTv.text = item.price
        }
    }
}