package com.pulse.components.analyzes.checkout

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.radiobutton.MaterialRadioButton
import com.pulse.R
import com.pulse.components.analyzes.checkout.AnalyzeCheckoutFragmentDirections.Companion.globalToHome
import com.pulse.components.analyzes.checkout.AnalyzeCheckoutFragmentDirections.Companion.globalToPromoCodeDialog
import com.pulse.components.checkout.dialog.PromoCodeDialogFragment
import com.pulse.components.checkout.model.TempPaymentMethod
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.data.remote.DummyData
import com.pulse.databinding.FragmentAnalyzeCheckoutBinding
import com.pulse.util.ColorFilterUtil
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzeCheckoutFragment(private val viewModel: AnalyzeCheckoutViewModel) : BaseMVVMFragment(R.layout.fragment_analyze_checkout) {

    private val args by navArgs<AnalyzeCheckoutFragmentArgs>()
    private val binding by viewBinding(FragmentAnalyzeCheckoutBinding::bind)
    private val radioButtonPadding by lazy { resources.getDimension(R.dimen._8sdp).toInt() }

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
        mbChangeClinic.mockToast()
        viewDateTime.mockToast() // TODO date dialog
//        viewDateTime.setOnClick {
//
//        }
        viewDateTime.detailText = "27 февраля, 11:00"
        mbPromoCode.setDebounceOnClickListener {
            setFragmentResultListener(PromoCodeDialogFragment.PROMO_CODE_REQUEST_KEY) { _, bundle ->
                val code = bundle[PromoCodeDialogFragment.PROMO_CODE_EXTRA_KEY]
                viewModel.handlePromoCodeResult(code as String)
            }
            doNav(globalToPromoCodeDialog())
        }
        initPaymentMethods()
        mtvAddMethods.mockToast()
        val totalAmount = "${clinic.servicePrice} ₸"
        mtvTotalAmount.text = totalAmount
        val totalCost = "${clinic.servicePrice} ₸"
        mtvTotalPayable.text = totalCost
        mbCheckout.onClickDebounce {
            // TODO set proper action and dialog
            showAlert("Для подтверждения записи дождитесь звонка оператора") {
                title = "Ваша заявка принята!"
                positive = "Ok"
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
    }
}