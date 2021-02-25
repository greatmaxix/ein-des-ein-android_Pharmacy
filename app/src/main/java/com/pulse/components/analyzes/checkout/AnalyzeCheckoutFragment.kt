package com.pulse.components.analyzes.checkout

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.radiobutton.MaterialRadioButton
import com.pulse.R
import com.pulse.components.analyzes.checkout.AnalyzeCheckoutFragmentDirections.Companion.fromAnalyzeCheckoutToAddCard
import com.pulse.components.analyzes.checkout.AnalyzeCheckoutFragmentDirections.Companion.globalToClinicTabs
import com.pulse.components.analyzes.checkout.AnalyzeCheckoutFragmentDirections.Companion.globalToHome
import com.pulse.components.analyzes.checkout.AnalyzeCheckoutFragmentDirections.Companion.globalToPromoCodeDialog
import com.pulse.components.checkout.dialog.PromoCodeDialogFragment
import com.pulse.components.checkout.dialog.PromoCodeDialogFragment.Companion.PROMO_CODE_EXTRA_KEY
import com.pulse.components.checkout.model.TempPaymentMethod
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.data.remote.DummyData
import com.pulse.databinding.FragmentAnalyzeCheckoutBinding
import com.pulse.util.ColorFilterUtil
import org.koin.core.component.KoinApiExtension
import java.time.LocalDateTime

@KoinApiExtension
class AnalyzeCheckoutFragment(private val viewModel: AnalyzeCheckoutViewModel) : BaseMVVMFragment(R.layout.fragment_analyze_checkout) {

    private val args by navArgs<AnalyzeCheckoutFragmentArgs>()
    private val binding by viewBinding(FragmentAnalyzeCheckoutBinding::bind)
    private val radioButtonPadding by lazy { getDimensionPixelSize(R.dimen._8sdp) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

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
        initPaymentMethods()
        mtvAddMethods.onClickDebounce { navController.navigate(fromAnalyzeCheckoutToAddCard()) }
        val totalAmount = clinic.servicePrice.toPrice()
        mtvTotalAmount.text = totalAmount
        val totalCost = clinic.servicePrice.toPrice()
        mtvTotalPayable.text = totalCost
        mbCheckout.onClickDebounce {
            showAlertRes(R.string.your_request_accepted) {
                title = R.string.confirm_analyze_title
                positive = R.string.common_okButton
                positiveAction = {
                    navController.navigate(globalToHome())
                }
            }
        }
    }

    private fun initPaymentMethods() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        DummyData.paymentMethod.forEach {
            binding.rgPaymentMethods.addView(createPaymentRadioButton(layoutParams, it))
        }
    }

    private fun createPaymentRadioButton(layoutParams: ViewGroup.LayoutParams, it: TempPaymentMethod) = MaterialRadioButton(requireContext()).apply {
        this.layoutParams = layoutParams
        setPadding(radioButtonPadding, (radioButtonPadding * 1.5).toInt(), radioButtonPadding, (radioButtonPadding * 1.5).toInt())
        text = it.name
        val drawable = getDrawable(it.icon)?.apply { if (!it.isChecked) colorFilter = ColorFilterUtil.blackWhiteFilter }
        setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        setTextColor(getColorStateList(R.color.selector_text_payment))
        buttonTintList = getColorStateList(R.color.selector_tint_button_payment)
        isEnabled = it.isChecked
        isChecked = it.isChecked
    }

    override fun onBindLiveData() {
        observe(viewModel.customerInfoLiveData) { customer ->
            customer?.let {
                binding.viewUserDetails.setData(it.name, it.phone, it.email)
            }
        }
        observe(viewModel.pickDateTimeLiveData) {
            DatePickerDialog(requireContext(), { _, year, month, day ->
                TimePickerDialog(requireContext(), { _, hour, minute ->
                    val pickedDateTime = LocalDateTime.of(year, month + 1, day, hour, minute)
                    viewModel.setDateTime(pickedDateTime)
                }, it.hour, it.minute, DateFormat.is24HourFormat(requireContext())).show()
            }, it.year, it.monthValue - 1, it.dayOfMonth).show()
        }
        observe(viewModel.selectedDateTimeLiveData) {
            binding.viewDateTime.detailText = it.analyzeCheckoutDate
        }
    }
}