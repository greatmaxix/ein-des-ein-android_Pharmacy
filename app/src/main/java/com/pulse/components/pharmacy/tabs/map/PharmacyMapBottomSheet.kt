package com.pulse.components.pharmacy.tabs.map

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.pharmacy.tabs.BaseTabFragment.Companion.PHARMACY_KEY
import com.pulse.core.base.mvvm.BaseMVVMFragmentDialogBottomSheet
import com.pulse.core.extensions.*
import com.pulse.databinding.ItemPharmacyBinding

class PharmacyMapBottomSheet : BaseMVVMFragmentDialogBottomSheet(R.layout.item_pharmacy) {

    private val args: PharmacyMapBottomSheetArgs by navArgs()
    private val binding by viewBinding(ItemPharmacyBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        with(args.pharmacy) {
            ivPharmacy.loadGlideDrawableByURL(logo.url)
            mtvName.text = name
            mtvAddress.text = location.address
            mtvPhone.text = getString(R.string.pharmacyPhoneWith, phone)
            mtvPhone.setDebounceOnClickListener { showDial(phone) }
            mtvProductPrice.text = getString(R.string.price, firstProductPrice)
            fabLocation.setDebounceOnClickListener { showDirection(location.lat, location.lng) }
            mbAddToCart.setDebounceOnClickListener { notifySavedStateHandle(PHARMACY_KEY, args.pharmacy.pharmacyProducts?.first()?.pharmacyProductId) }
        }
    }
}