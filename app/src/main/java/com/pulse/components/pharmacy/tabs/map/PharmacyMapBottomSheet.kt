package com.pulse.components.pharmacy.tabs.map

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.pulse.R
import com.pulse.components.pharmacy.tabs.BaseTabFragment.Companion.PHARMACY_KEY
import com.pulse.core.base.mvvm.BaseMVVMFragmentDialogBottomSheet
import com.pulse.core.extensions.*
import kotlinx.android.synthetic.main.item_pharmacy.*

class PharmacyMapBottomSheet : BaseMVVMFragmentDialogBottomSheet(R.layout.item_pharmacy) {

    private val args: PharmacyMapBottomSheetArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(args.pharmacy) {
            ivPharmacy.loadGlideDrawableByURL(logo.url)
            tvPharmacyName.text = name
            tvPharmacyAddress.text = location.address
            tvPharmacyPhone.text = getString(R.string.pharmacyPhoneWith, phone)
            tvPharmacyPhone.onClick { showDial(phone) }

            tvProductPrice.text = getString(R.string.price, firstProductPrice)

            ivPharmacyLocation.onClick { showDirection(location.lat, location.lng) }

            mbProductAddToCart.onClick { notifySavedStateHandle(PHARMACY_KEY, args.pharmacy.pharmacyProducts?.first()?.pharmacyProductId) }
        }
    }
}