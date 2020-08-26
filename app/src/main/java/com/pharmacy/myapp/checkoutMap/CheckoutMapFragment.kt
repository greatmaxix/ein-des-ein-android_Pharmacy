package com.pharmacy.myapp.checkoutMap

import android.graphics.*
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.pharmacy.myapp.R
import com.pharmacy.myapp.checkoutMap.model.TempAvailableDrugstore
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.dimensionPixelSize
import com.pharmacy.myapp.core.extensions.sharedGraphViewModel
import kotlinx.android.synthetic.main.fragment_checkout_map.*
import org.koin.android.ext.android.get

class CheckoutMapFragment : BaseMVVMFragment(R.layout.fragment_checkout_map) {

    private val viewModel: CheckoutMapViewModel by sharedGraphViewModel(R.id.checkout_map_graph)
    private var map: GoogleMap? = null
    private val mapZoom = 18F
    private val drawTextSize = 14

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mapViewCheckout.onStart()
    }

    override fun onResume() {
        mapViewCheckout.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapViewCheckout.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapViewCheckout.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapViewCheckout?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapViewCheckout.onLowMemory()
        super.onLowMemory()
    }

    override fun onBindLiveData() {
        viewModel.drugstoresLiveData.observeExt(::showMarkers)
        viewModel.showBottomSheetLiveData.observeExt {
            get<DrugstoreBottomSheet>().show(childFragmentManager)
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(it.latLng, mapZoom))
        }
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mapViewCheckout.onCreate(savedInstanceState)
        mapViewCheckout.getMapAsync {
            viewModel.getDrugstores()
            map = it
            map?.setOnMarkerClickListener { marker ->
                viewModel.markerClicked(marker.tag as Int)
                true
            }
        }
    }

    private fun showMarkers(list: ArrayList<TempAvailableDrugstore>) {
        list.forEach(::addMarkerToMap)
        map?.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsFromLatLngList(list.map { it.latLng }), dimensionPixelSize(R.dimen._36sdp)))
    }

    private fun addMarkerToMap(drugstore: TempAvailableDrugstore) {
        val icon = BitmapDescriptorFactory.fromBitmap(drawTextToBitmap(drugstore.price))
        val marker = MarkerOptions().title(drugstore.price).position(drugstore.latLng).icon(icon)
        map?.addMarker(marker)?.apply { tag = drugstore.id }
    }

    private fun drawTextToBitmap(text: String): Bitmap? {
        val scale: Float = resources.displayMetrics.density
        var bitmap = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_marker_icon)?.toBitmap() ?: return null
        var bitmapConfig = bitmap.config
        if (bitmapConfig == null) bitmapConfig = Bitmap.Config.ARGB_8888
        bitmap = bitmap.copy(bitmapConfig, true)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        paint.textSize = (drawTextSize * scale)
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE)

        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        val centerX = (bitmap.width - bounds.width()) / 2
        val centerY = (bitmap.height + bounds.height()) / 2.8
        canvas.drawText(text, centerX.toFloat(), centerY.toFloat(), paint)
        return bitmap
    }

    private fun boundsFromLatLngList(list: List<LatLng>): LatLngBounds {
        var bottomRightLatitude = list.first().latitude
        var topLeftLatitude = bottomRightLatitude
        var bottomRightLongitude = list.first().longitude
        var topLeftLongitude = bottomRightLongitude
        for (latLng in list) {
            if (latLng.latitude > topLeftLatitude) topLeftLatitude = latLng.latitude
            if (latLng.latitude < bottomRightLatitude) bottomRightLatitude = latLng.latitude
            if (latLng.longitude > topLeftLongitude) topLeftLongitude = latLng.longitude
            if (latLng.longitude < bottomRightLongitude) bottomRightLongitude = latLng.longitude
        }
        return LatLngBounds(LatLng(bottomRightLatitude, bottomRightLongitude), LatLng(topLeftLatitude, topLeftLongitude))
    }

}