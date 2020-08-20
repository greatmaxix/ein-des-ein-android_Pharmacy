package com.pharmacy.myapp.cart

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.setTopRoundCornerBackground
import com.pharmacy.myapp.core.extensions.toast
import com.pharmacy.myapp.data.DummyData
import com.pharmacy.myapp.ui.decoration.SwipeHandler
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : BaseMVVMFragment(R.layout.fragment_cart) {

    private val cartAdapter = CartAdapter()
    private val itemTouchHelper by lazy { ItemTouchHelper(SwipeHandler { /*viewModel.remove(it)*/ }) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()

        initOrderProducts()
        bottomLayoutCart.setTopRoundCornerBackground()
        chooseStoreBtn.onClick { requireContext().toast("TODO: Choose store") }
        mcvRecipeContainer.onClick { requireContext().toast("TODO: Recipe") }
    }

    private fun initOrderProducts() {
        val items = DummyData.getOrderProducts()
        rvQuickAccess.setHasFixedSize(true)
        rvQuickAccess.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvQuickAccess.adapter = cartAdapter
        itemTouchHelper.attachToRecyclerView(rvQuickAccess)
        cartAdapter.setList(items)
    }

}