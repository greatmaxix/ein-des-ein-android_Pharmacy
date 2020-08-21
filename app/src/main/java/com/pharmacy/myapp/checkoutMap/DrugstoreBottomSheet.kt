package com.pharmacy.myapp.checkoutMap

import com.pharmacy.myapp.MainGraphDirections.Companion.globalToCheckout
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragmentDialogBottomSheet
import com.pharmacy.myapp.core.extensions.onClick
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import com.pharmacy.myapp.core.extensions.textColor
import com.pharmacy.myapp.core.extensions.toast
import kotlinx.android.synthetic.main.item_checkout_store.*

class DrugstoreBottomSheet : BaseMVVMFragmentDialogBottomSheet(R.layout.item_checkout_store) {

    private val viewModel: CheckoutMapViewModel by sharedGraphViewModel(R.id.checkout_map_graph)

    override fun onBindLiveData() {
        viewModel.selectedDrugstoreLiveData.observe { drugstore ->
            tvAvailabilityInDrugstore.textColor(drugstore.availabilityColor())
            tvAvailabilityInDrugstore.text = drugstore.availability
            tvDrugstoreName.text = drugstore.name
            tvDrugstoreAddress.text = drugstore.address
            tvContactInformation.text = drugstore.contactInfo
            tvWorkingHours.text = drugstore.workingHours
            tvPrice.text = drugstore.price
            btnChooseDrugstore.onClick { viewModel.setDirection(globalToCheckout()) }
            ivDrugstoreLocation.onClick { context?.toast("TODO: Location") }
        }
    }

}