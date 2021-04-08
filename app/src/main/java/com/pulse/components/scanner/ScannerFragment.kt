package com.pulse.components.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.budiyev.android.codescanner.*
import com.pulse.R
import com.pulse.components.product.BaseProductFragment
import com.pulse.components.scanner.ScannerFragmentDirections.Companion.fromScannerToListResult
import com.pulse.core.extensions.animateVisibleOrGone
import com.pulse.core.extensions.doWithDelay
import com.pulse.core.extensions.observe
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.databinding.FragmentQrCodeScannerBinding
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import timber.log.Timber

@ExperimentalCoroutinesApi
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
            requestPermissions(
                firstPermission = Manifest.permission.CAMERA,
                openSettingsMessage = R.string.cameraPermissionPermanentlyDenied,
                rationaleMessage = R.string.cameraPermissionRationaleMessage,
                deniedMessage = R.string.cameraPermissionDenied
            ) { grant.invoke() }
        } else {
            uiHelper.showMessage(getString(R.string.cameraPermissionNoCameraOnDevice))
        }
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