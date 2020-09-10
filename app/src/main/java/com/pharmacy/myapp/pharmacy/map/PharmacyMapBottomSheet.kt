package com.pharmacy.myapp.pharmacy.map

import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragmentDialogBottomSheet

class PharmacyMapBottomSheet : BaseMVVMFragmentDialogBottomSheet(R.layout.item_checkout_store) {

    //private val viewModel: CheckoutMapViewModel  by lazy { requireParentFragment().getViewModel() }

    override fun onBindLiveData() {
       /* viewModel.selectedDrugstoreLiveData.observe { drugstore ->
            tvAvailabilityInDrugstore.textColor(drugstore.availabilityColor())
            tvAvailabilityInDrugstore.text = drugstore.availability
            tvDrugstoreName.text = drugstore.name
            tvDrugstoreAddress.text = drugstore.address
            tvContactInformation.text = drugstore.contactInfo
            tvWorkingHours.text = drugstore.workingHours
            tvPrice.text = drugstore.price
            btnChooseDrugstore.onClick { viewModel.setDirection(globalToCheckout()) }
            ivDrugstoreLocation.onClick { context?.toast("TODO: Location") }
        }*/
    }

}