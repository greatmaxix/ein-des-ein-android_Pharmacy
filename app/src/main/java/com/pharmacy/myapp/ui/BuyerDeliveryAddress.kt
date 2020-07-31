package com.pharmacy.myapp.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkout.model.TempDeliveryAddress
import com.pharmacy.myapp.checkout.model.TempPharmacyAddress
import com.pharmacy.myapp.core.extensions.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_buyer_deliver_address.view.*

class BuyerDeliveryAddress @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LayoutContainer {

    override val containerView: View? = inflate(R.layout.view_buyer_deliver_address, true)

    private var editMode: Boolean? = null
        set(value) {
            field = value
            if (value == null) return
            updateFieldsContent()
            groupInputFieldsCheckout.animateVisibleOrGone(value)
            groupFilledDeliveryAddressCheckout.animateVisibleOrGone(!value)
        }

    var deliveryAddress: TempDeliveryAddress = TempDeliveryAddress.empty()
        private set
    var pharmacyAddress: TempPharmacyAddress? = null
        private set
    var deliveryMethod: DeliveryMethod = DeliveryMethod.DELIVERY
        private set

    init {
        tilCityCheckout.editText?.doAfterTextChanged { deliveryAddress.city = it.toString() }
        tilStreetCheckout.editText?.doAfterTextChanged { deliveryAddress.street = it.toString() }
        tilHouseNoCheckout.editText?.doAfterTextChanged { deliveryAddress.houseNo = it.toString() }
        tilApartmentNoCheckout.editText?.doAfterTextChanged { deliveryAddress.apartmentNo = it.toString() }
        tvBuyerDeliveryAddressEditCheckout.onClick {
            if (deliveryMethod == DeliveryMethod.DELIVERY) {
                editMode = true
            } else {
                // TODO delegate click to fragment
                context.toast("TODO open pharmacy list")
            }
        }
        ivStreetMapCheckout.onClick {
            context.toast("TODO open map")
            // TODO delegate click to fragment
        }
        ivFilledAddressMapCheckout.onClick {
            context.toast("TODO open map")
            // TODO delegate click to fragment
        }
    }

    fun setData(deliveryAddress: TempDeliveryAddress? = null, pharmacyAddress: TempPharmacyAddress? = null) {
        if (deliveryAddress != null) {
            this.deliveryAddress = deliveryAddress
            if (deliveryMethod == DeliveryMethod.DELIVERY) {
                editMode = deliveryAddress.isEmpty()
            }
        }
        this.pharmacyAddress = pharmacyAddress
        updateFieldsContent()
    }

    private fun updateFieldsContent() {
        if (editMode != false) {
            with(deliveryAddress) {
                tilCityCheckout.editText?.setText(city)
                tilStreetCheckout.editText?.setText(street)
                tilHouseNoCheckout.editText?.setText(houseNo)
                tilApartmentNoCheckout.editText?.setText(apartmentNo)
            }
        } else {
            if (deliveryMethod == DeliveryMethod.DELIVERY) {
                ivPharmacyLogoCheckout.gone()
                tvProductAvailabilityCheckout.gone()
                tvPharmacyNameCheckout.gone()
                tvPharmacyAddressCheckout.gone()

                with(deliveryAddress) {
                    val cityAndStreetHolder = "\uD83C\uDFE0 $street $houseNo, $city"
                    tvBuyerCityStreetCheckout.text = cityAndStreetHolder
                    val houseAndApartmentHolder = "\uD83D\uDEAA $houseNo • $apartmentNo"
                    tvCommonInfoLineCheckout.text = houseAndApartmentHolder
                    val timingHolder = "⏰ $timing"
                    tvBuyerTimingCheckout.text = timingHolder
                }
                tvBuyerCityStreetCheckout.visible()
            } else {
                tvBuyerCityStreetCheckout.gone()

                // TODO check this case
                pharmacyAddress?.let {
                    with(it) {
                        Glide.with(context)
                            .load(imageUrl)
                            .into(ivPharmacyLogoCheckout)
                        tvProductAvailabilityCheckout.text = availability
                        tvPharmacyNameCheckout.text = name
                        val cityAndStreetHolder = "\uD83C\uDFE0 $street $houseNo, $city"
                        tvPharmacyAddressCheckout.text = cityAndStreetHolder
                        val phoneHolder = "\uD83D\uDCDE️ $phone"
                        tvCommonInfoLineCheckout.text = phoneHolder
                        val timingHolder = "⏰ $timing"
                        tvBuyerTimingCheckout.text = timingHolder
                    }
                    ivPharmacyLogoCheckout.visible()
                    tvProductAvailabilityCheckout.visible()
                    tvPharmacyNameCheckout.visible()
                    tvPharmacyAddressCheckout.visible()
                }
            }
        }
    }

    fun changeDeliveryMethod(deliveryMethod: DeliveryMethod) {
        this.deliveryMethod = deliveryMethod
        updateFieldsContent()
    }

    enum class DeliveryMethod {
        DELIVERY,
        PICKUP
    }
}