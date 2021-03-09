package com.pulse.components.pharmacy.tabs.map

import android.graphics.*
import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.pulse.R
import com.pulse.components.pharmacy.model.Pharmacy
import com.pulse.components.pharmacy.tabs.BaseTabFragment
import com.pulse.core.extensions.asBitmap
import com.pulse.core.extensions.asyncWithContext
import com.pulse.core.extensions.getDimensionPixelSize
import com.pulse.core.utils.BoundsUtils.boundsFromLatLngList
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
        MarkerOptions().position(pharmacy.location.mapCoordinates).icon(BitmapDescriptorFactory.fromBitmap(pharmacy.firstProductPrice.asBitmap(requireContext(), paint)))
}