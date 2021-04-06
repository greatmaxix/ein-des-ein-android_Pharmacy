package com.pulse.components.analyzes.list.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.list.model.Analyze
import com.pulse.core.base.adapter.BaseViewHolder
import com.pulse.core.extensions.*
import com.pulse.databinding.ItemAnalyzeBinding

class AnalyzesViewHolder(
    itemView: View,
    private val makeCall: (String) -> Unit
) : BaseViewHolder<Analyze>(itemView) {

    private val binding by viewBinding(ItemAnalyzeBinding::bind)

    override fun bind(item: Analyze) = with(binding) {
        mtvOrderNo.text = context.getString(R.string.order_num_holder, item.orderNo)
        mtvCategory.text = item.category.name
        mtvDateTime.text = item.dateTime.analyzeDate
        ivClinic.loadGlideCircle(item.clinic.logo.url) // TODO set placeholder
        mtvName.text = item.clinic.name
        mtvAddress.text = item.clinic.location.address
        mtvPhone.text = getString(R.string.pharmacyPhoneWith, item.clinic.phone)
        mtvPhone.setDebounceOnClickListener { makeCall(item.clinic.phone) }
        mtvProductPrice.text = getString(R.string.price, item.totalPrice)
    }

    companion object {

        fun newInstance(parent: ViewGroup, makeCall: (String) -> Unit) = AnalyzesViewHolder(parent.inflate(R.layout.item_analyze), makeCall)
    }
}
