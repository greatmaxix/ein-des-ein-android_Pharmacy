package com.pulse.components.analyzes.clinic.tabs.map

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
import com.pulse.components.analyzes.clinic.tabs.BaseClinicTabFragment
import com.pulse.components.analyzes.details.model.Clinic
import com.pulse.core.extensions.asBitmap
import com.pulse.core.extensions.asyncWithContext
import com.pulse.core.extensions.getDimensionPixelSize
import com.pulse.core.utils.BoundsUtils.boundsFromLatLngList
import com.pulse.databinding.FragmentPharmacyMapBinding
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class ClinicMapFragment : BaseClinicTabFragment(R.layout.fragment_clinic_map), OnMapReadyCallback {

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
        mapPharmacy.getMapAsync(this@ClinicMapFragment)
    }

    override fun onResume() {
        super.onResume()
        binding.mapPharmacy.onResume()
    }

    override fun onPause() {
        binding.mapPharmacy.onPause()
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        binding.mapPharmacy.onStart()
    }

    override fun onStop() {
        binding.mapPharmacy.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        binding.mapPharmacy.onDestroy()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        binding.mapPharmacy.onLowMemory()
        super.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.mapPharmacy.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        observeResult(viewModel.clinicsListLiveData) {
            onEmmit = { showMarkers(this) }
        }

        googleMap?.setOnMarkerClickListener { marker ->
            viewModel.getClinic(marker.tag as Int)
            true
        }
    }

    private fun showMarkers(list: List<Clinic>) {
        list.forEach { asyncWithContext({ createMarker(it) }, { googleMap?.addMarker(this)?.apply { tag = it.id } }) }
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsFromLatLngList(list.map { it.location.mapCoordinates }), getDimensionPixelSize(R.dimen._36sdp)))
    }

    private fun createMarker(clinic: Clinic) =
        MarkerOptions().position(clinic.location.mapCoordinates).icon(BitmapDescriptorFactory.fromBitmap(clinic.servicePrice.toString().asBitmap(requireContext(), paint)))
}