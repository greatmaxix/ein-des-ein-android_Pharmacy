package com.pulse.components.analyzes.checkout

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.checkout.AnalyzeCheckoutFragmentDirections.Companion.globalToClinicTabs
import com.pulse.components.analyzes.checkout.AnalyzeCheckoutFragmentDirections.Companion.globalToHome
import com.pulse.components.analyzes.checkout.AnalyzeCheckoutFragmentDirections.Companion.globalToPromoCodeDialog
import com.pulse.components.checkout.dialog.PromoCodeDialogFragment
import com.pulse.components.checkout.dialog.PromoCodeDialogFragment.Companion.PROMO_CODE_EXTRA_KEY
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentAnalyzeCheckoutBinding
import org.koin.core.component.KoinApiExtension
import java.time.LocalDateTime

@KoinApiExtension
class AnalyzeCheckoutFragment :
    BaseToolbarFragment<AnalyzeCheckoutViewModel>(R.layout.fragment_analyze_checkout, AnalyzeCheckoutViewModel::class) {

    private val args by navArgs<AnalyzeCheckoutFragmentArgs>()
    private val binding by viewBinding(FragmentAnalyzeCheckoutBinding::bind)

    override fun initUI() = with(binding) {
        showBackButton()
        val category = args.category
        val clinic = args.clinic

        with(viewAnalyzeCategory) {
            mtvTitle.text = category.name
            mtvSubtitle.text = category.code
            ivArrow.gone()
        }
        with(viewClinic) {
            ivClinic.loadGlideCircle(clinic.logo.url, R.drawable.ic_placeholder_search) // TODO set placeholder
            mtvName.text = clinic.name
            mtvAddress.text = clinic.location.address
            mtvPhone.text = getString(R.string.pharmacyPhoneWith, clinic.phone)
            mtvPhone.setDebounceOnClickListener { showDial(clinic.phone) }
            fabLocation.gone()
        }
        mbChangeClinic.onClickDebounce { navController.navigate(globalToClinicTabs(args.category)) }
        viewDateTime.onClickDebounce { viewModel.pickDateTime() }
        mbPromoCode.setDebounceOnClickListener {
            setFragmentResultListener(PromoCodeDialogFragment.PROMO_CODE_REQUEST_KEY) { _, bundle ->
                viewModel.handlePromoCodeResult(bundle.getString(PROMO_CODE_EXTRA_KEY) ?: "")
            }
            navController.navigate(globalToPromoCodeDialog())
        }
        val totalAmount = clinic.servicePrice.toPrice()
        mtvTotalAmount.text = totalAmount
        val totalCost = clinic.servicePrice.toPrice()
        mtvTotalPayable.text = totalCost
        mbCheckout.onClickDebounce {
            uiHelper.showDialog(getString(R.string.confirm_analyze_title)) {
                title = getString(R.string.your_request_accepted)
                positive = getString(R.string.common_okButton)
                positiveAction = {
                    navController.navigate(globalToHome())
                }
            }
        }
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.customerInfoState) { customer ->
            customer?.let {
                binding.viewUserDetails.setData(it.name, it.phone, it.email)
            }
        }
        observe(viewModel.pickDateTimeState) {
            it?.let {
                DatePickerDialog(requireContext(), R.style.PickerStyle, { _, year, month, day ->
                    TimePickerDialog(requireContext(), R.style.PickerStyle, { _, hour, minute ->
                        val pickedDateTime = LocalDateTime.of(year, month + 1, day, hour, minute)
                        viewModel.setDateTime(pickedDateTime)
                    }, it.hour, it.minute, DateFormat.is24HourFormat(requireContext())).show()
                }, it.year, it.monthValue - 1, it.dayOfMonth).show()
            }
        }
        observe(viewModel.selectedDateTimeState) {
            it?.let { binding.viewDateTime.detailText = it.analyzeCheckoutDate }
        }
    }
}