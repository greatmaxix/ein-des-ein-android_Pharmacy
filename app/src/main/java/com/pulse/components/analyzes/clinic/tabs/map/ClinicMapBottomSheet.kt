package com.pulse.components.analyzes.clinic.tabs.map

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragmentDialogBottomSheet
import com.pulse.core.extensions.*
import com.pulse.databinding.ItemClinicBinding

class ClinicMapBottomSheet : BaseMVVMFragmentDialogBottomSheet(R.layout.item_clinic) {

    private val args: ClinicMapBottomSheetArgs by navArgs()
    private val binding by viewBinding(ItemClinicBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        with(args.clinic) {
            ivClinic.loadGlideCircle(logo.url, R.drawable.ic_placeholder_search) // TODO set placeholder
            mtvName.text = name
            mtvAddress.text = location.address
            mtvPhone.text = getString(R.string.pharmacyPhoneWith, phone)
            mtvPhone.setDebounceOnClickListener { showDial(phone) }
            mtvProductPrice.text = getString(R.string.price, servicePrice.toString())
            mtvProductPrice.visible()
            mbOrderService.visible()
            mbOrderService.setDebounceOnClickListener {
                setFragmentResult(KEY_CLINIC_MAP_BOTTOM_SHEET, bundleOf(KEY_ORDER_SERVICE to args.clinic))
                dismiss()
            }
            fabLocation.setDebounceOnClickListener { showDirection(location.lat, location.lng) }
        }
    }

    companion object {

        const val KEY_CLINIC_MAP_BOTTOM_SHEET = "CLINIC_MAP_BOTTOM_SHEET"
        const val KEY_ORDER_SERVICE = "ORDER_SERVICE"
    }
}