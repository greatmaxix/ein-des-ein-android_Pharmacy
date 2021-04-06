package com.pulse.components.analyzes.order

import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.checkout.model.PaymentMethodAdapterModel
import com.pulse.components.payments.model.PaymentMethod
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentAnalyzeOrderBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzeOrderFragment : BaseToolbarFragment<AnalyzeOrderViewModel>(R.layout.fragment_analyze_order, AnalyzeOrderViewModel::class) {

    private val args by navArgs<AnalyzeOrderFragmentArgs>()
    private val binding by viewBinding(FragmentAnalyzeOrderBinding::bind)

    override fun initUI()  = with(binding) {
        showBackButton()
        with(args.order) {
            toolbar.toolbar.title = getString(R.string.order_num_holder, orderNo)
            viewBuyerInfo.customer = customer
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
            viewDateTime.detailText = dateTime.analyzeCheckoutDate
            PaymentMethodAdapterModel(PaymentMethod.CASH, true).let { // TODO change to proper value
                mtvPaymentType.text = getString(it.method.title)
                mtvPaymentType.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, it.method.icon, 0)
            }
            mtvOrderNote.text = note
            val totalAmount = clinic.servicePrice.toPrice()
            mtvTotalAmount.text = totalAmount
            val totalCost = clinic.servicePrice.toPrice()
            mtvTotalPayable.text = totalCost
            mbCancel.onClickDebounce {
                showAlertRes(R.string.thanks_for_using_pulse) {
                    title = R.string.request_cancelled
                    positive = R.string.common_okButton
                    positiveAction = {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}