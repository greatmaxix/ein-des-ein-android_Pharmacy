package com.pharmacy.myapp.qrCodeScanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.*
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.fragment.dialog.AlertDialogFragment
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import kotlinx.android.synthetic.main.fragment_qr_code_scanner.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import timber.log.Timber

class QrCodeScannerFragment(private val viewModel: QrCodeScannerViewModel) : BaseMVVMFragment(R.layout.fragment_qr_code_scanner) {

    private var codeScanner: CodeScanner? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        goToScanBtn.onClick { viewModel.descriptionViewed() }
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
                            setPositiveListener(DialogOnClick { _, _ ->
                                request.send()
                            })
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
            setPositiveListener(DialogOnClick { _, _ ->
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", requireActivity().packageName, null)
                })
            })
        }.show(childFragmentManager)
    }

    override fun onBindLiveData() {
        super.onBindLiveData()

        viewModel.directionLiveData.observeExt(navController::navigate)
        viewModel.errorLiveData.observeExt { messageCallback?.showError(it) }
        viewModel.progressLiveData.observeExt { progressCallback?.setInProgress(it) }
        viewModel.descriptionVisibility.observeExt { qrCodeScannerInstructionGroup.animateVisibleOrGone(it) }
        viewModel.searchResultItem.observeExt {
            SearchResultBottomSheet.newInstance {
                codeScanner?.startPreview()
            }.show(childFragmentManager)
        }
    }

    private fun initQRCamera() {
        codeScanner = CodeScanner(requireContext(), codeScannerView)
            .apply {
                camera = CodeScanner.CAMERA_BACK // TODO check on devices with only front camera available
                formats = CodeScanner.ALL_FORMATS
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
                    lifecycleScope.launch(Main.immediate) {
                        Timber.e(it, "Error scanning qr code")
                        messageCallback?.showError(getString(R.string.qrCodeScannerError)) {
                            navController.popBackStack()
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
}