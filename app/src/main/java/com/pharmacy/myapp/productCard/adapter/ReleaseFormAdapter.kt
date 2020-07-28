package com.pharmacy.myapp.productCard.adapter

import android.view.View
import android.view.ViewGroup
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.adapter.BaseRecyclerAdapter
import com.pharmacy.myapp.core.base.adapter.BaseViewHolder
import com.pharmacy.myapp.core.extensions.inflate
import com.pharmacy.myapp.productCard.model.TempReleaseFormModel
import kotlinx.android.synthetic.main.item_release_form.view.*

class ReleaseFormAdapter : BaseRecyclerAdapter<TempReleaseFormModel, ReleaseFormAdapter.ReleaseFormViewHolder>() {

    fun setList(list: MutableList<TempReleaseFormModel>) {
        items = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReleaseFormViewHolder = ReleaseFormViewHolder.newInstance(parent)

    class ReleaseFormViewHolder(view: View) : BaseViewHolder<TempReleaseFormModel>(view) {

        override fun bind(item: TempReleaseFormModel) {
            itemView.nameTv.text = item.name
            itemView.priceTv.text = item.price
        }

        companion object {

            fun newInstance(parent: ViewGroup) = ReleaseFormViewHolder(parent.inflate(R.layout.item_release_form))
        }
    }
}