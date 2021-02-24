package com.pulse.components.analyzes.order

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.components.analyzes.order.AnalyzeOrderFragmentDirections.Companion.globalToHome
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.data.remote.DummyData
import com.pulse.databinding.FragmentAnalyzeOrderBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class AnalyzeOrderFragment(private val viewModel: AnalyzeOrderViewModel) : BaseMVVMFragment(R.layout.fragment_analyze_order) {

    private val args by navArgs<AnalyzeOrderFragmentArgs>()
    private val binding by viewBinding(FragmentAnalyzeOrderBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        with(args.order) {
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

            DummyData.paymentMethod[2].let { // TODO change to proper value
                mtvPaymentType.text = it.name
                mtvPaymentType.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, it.icon, 0)
            }
            mtvOrderNote.text = note
            val totalAmount = "${clinic.servicePrice} ₸"
            mtvTotalAmount.text = totalAmount
            val totalCost = "${clinic.servicePrice} ₸"
            mtvTotalPayable.text = totalCost
            mbCancel.onClickDebounce {
                // TODO set proper action and dialog
                showAlert("Запись отменена") {
                    title = "Спасибо за использование Pulse"
                    positive = "Ok"
                    positiveAction = {
                        navController.navigate(globalToHome())
                    }
                }
            }
        }
    }
}