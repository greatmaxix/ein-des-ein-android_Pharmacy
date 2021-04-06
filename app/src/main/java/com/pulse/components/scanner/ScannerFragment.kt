package com.pulse.components.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.budiyev.android.codescanner.*
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pulse.R
import com.pulse.components.product.BaseProductFragment
import com.pulse.components.scanner.ScannerFragmentDirections.Companion.fromScannerToListResult
import com.pulse.core.base.fragment.dialog.AlertDialogFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentQrCodeScannerBinding
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import timber.log.Timber

@KoinApiExtension
class ScannerFragment : BaseProductFragment<ScannerViewModel>(R.layout.fragment_qr_code_scanner, ScannerViewModel::class) {

    private val binding by viewBinding(FragmentQrCodeScannerBinding::bind)
    private var codeScanner: CodeScanner? = null

    override fun initUI() = with(binding) {
        showBackButton()
        mbGoToScan.setDebounceOnClickListener { viewModel.descriptionViewed() }
        checkCameraPermission { initQRCamera() }
    }

    private fun checkCameraPermission(grant: () -> Unit) {
        val isDeviceSupportCamera: Boolean = requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        if (isDeviceSupportCamera) {
            val request = permissionsBuilder(Manifest.permission.CAMERA).build()
            request.addListener { result ->
                when {
                    result.allGranted() -> grant.invoke()
                    result.anyDenied() -> requireContext().toast(R.string.cameraPermissionDenied)
                    result.anyPermanentlyDenied() -> openSettings()
                    result.anyShouldShowRationale() -> {
                        AlertDialogFragment.newInstance(
                            message = getString(R.string.cameraPermissionRationaleMessage),
                            positive = getString(R.string.grantPermissionButton),
                            negative = getString(R.string.closeButton)
                        ).apply {
                            setPositiveListener { _, _ -> request.send() }
                        }.show(childFragmentManager)
                    }
                }
            }
            request.send()
        } else {
            requireContext().toast(R.string.cameraPermissionNoCameraOnDevice)
        }
    }

    private fun openSettings() {
        AlertDialogFragment.newInstance(
            message = getString(R.string.cameraPermissionPermanentlyDenied),
            positive = getString(R.string.permissionDialogSettingsButton),
            negative = getString(R.string.permissionDialogCancel)
        ).apply {
            setPositiveListener { _, _ ->
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", requireActivity().packageName, null)
                })
            }
        }.show(childFragmentManager)
    }

    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.descriptionVisibilityState) { binding.groupInstruction.animateVisibleOrGone(it) }
        observe(viewModel.resultState) { navController.navigate(fromScannerToListResult(it.toTypedArray())) }
    }

    private fun initQRCamera() {
        codeScanner = CodeScanner(requireContext(), binding.viewCodeScanner)
            .apply {
                camera = CodeScanner.CAMERA_BACK // TODO check on devices with only front camera available
                formats = CodeScanner.ONE_DIMENSIONAL_FORMATS
                autoFocusMode = AutoFocusMode.SAFE
                scanMode = ScanMode.SINGLE
                isAutoFocusEnabled = true
                isFlashEnabled = false

                decodeCallback = DecodeCallback {
                    if (it.text.isNullOrBlank()) {
                        doWithDelay(500) { startPreview() }
                    } else {
                        viewModel.searchQrCode(it.text)
                    }
                }

                errorCallback = ErrorCallback {
                    viewLifecycleOwner.lifecycleScope.launch(Main.immediate) {
                        Timber.e(it, "Error scanning qr code")
                        uiHelper.showDialog(getString(R.string.qrCodeScannerError)) {
                            positiveAction = { navController.popBackStack() }
                        }
                    }
                }
                startPreview()
            }
    }

    override fun onResume() {
        super.onResume()
        codeScanner?.startPreview()
    }

    override fun onPause() {
        codeScanner?.releaseResources()
        super.onPause()
    }

    override fun notifyWish(globalProductId: Int) {
        // mock
    }
}