package com.pharmacy.myapp.myOrders

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.addItemDecorator
import com.pharmacy.myapp.data.remote.DummyData.getMyOrders
import kotlinx.android.synthetic.main.fragment_my_orders.*

class MyOrdersFragment : BaseMVVMFragment(R.layout.fragment_my_orders) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBackButton()
        rvOrdersListMyOrders.layoutManager = LinearLayoutManager(requireContext())
        rvOrdersListMyOrders.addItemDecorator()
        rvOrdersListMyOrders.adapter = MyOrdersAdapter().apply { setList(getMyOrders()) }
    }

}