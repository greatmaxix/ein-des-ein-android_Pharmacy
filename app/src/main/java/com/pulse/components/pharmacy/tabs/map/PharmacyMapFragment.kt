package com.pulse.components.pharmacy.tabs.map

import android.graphics.*
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.pulse.R
import com.pulse.components.pharmacy.model.Pharmacy
import com.pulse.components.pharmacy.tabs.BaseTabFragment
import com.pulse.core.extensions.asyncWithContext
import com.pulse.core.extensions.getDimensionPixelSize
import com.pulse.core.extensions.getDrawable
import com.pulse.databinding.FragmentPharmacyMapBinding

class PharmacyMapFragment : BaseTabFragment(R.layout.fragment_pharmacy_map), OnMapReadyCallback {

    private val binding by viewBinding(FragmentPharmacyMapBinding::bind)
    private var googleMap: GoogleMap? = null
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 14 * resources.displayMetrics.density // scale
            setShadowLayer(1f, 0f, 1f, Color.WHITE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        mapPharmacy.onCreate(savedInstanceState)
        mapPharmacy.getMapAsync(this@PharmacyMapFragment)
    }

    override fun onBindLiveData() {
        observeSavedStateHandler(PHARMACY_KEY, ::addProduct)
    }

    override fun onResume() {
        super.onResume()
        binding.mapPharmacy.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapPharmacy.onPause()
    }

    override fun onStart() {
        super.onStart()
        binding.mapPharmacy.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapPharmacy.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapPharmacy.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapPharmacy.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapPharmacy.onSaveInstanceState(outState)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        observeResult(viewModel.pharmacyListLiveData) {
            onEmmit = { showMarkers(this) }
        }

        googleMap?.setOnMarkerClickListener { marker ->
            viewModel.getPharmacy(marker.tag as Int)
            true
        }
    }

    private fun showMarkers(list: List<Pharmacy>) {
        list.forEach { asyncWithContext({ createMarker(it) }, { googleMap?.addMarker(this)?.apply { tag = it.id } }) }
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsFromLatLngList(list.map { it.location.mapCoordinates }), getDimensionPixelSize(R.dimen._36sdp)))
    }

    private fun createMarker(pharmacy: Pharmacy) =
        MarkerOptions().position(pharmacy.location.mapCoordinates).icon(BitmapDescriptorFactory.fromBitmap(pharmacy.firstProductPrice.asBitmap()))

    @Deprecated("use library")
    private fun String.asBitmap(): Bitmap? {
        var bitmap = getDrawable(R.drawable.ic_marker_icon)?.toBitmap() ?: return null
        bitmap = bitmap.copy(bitmap.config ?: Bitmap.Config.ARGB_8888, true)
        val bounds = Rect()
        paint.getTextBounds(this, 0, length, bounds)
        val centerX = (bitmap.width - bounds.width()) / 2f
        val centerY = (bitmap.height + bounds.height()) / 2.8f
        Canvas(bitmap).drawText(this, centerX, centerY, paint)
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