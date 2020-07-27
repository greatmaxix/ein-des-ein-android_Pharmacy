package com.pharmacy.myapp.profile.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.profile.ProfileViewModel
import com.pharmacy.myapp.ui.text.*
import com.pharmacy.myapp.util.BlurTransformation
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import timber.log.Timber

class EditProfileFragment : BaseMVVMFragment(R.layout.fragment_profile_edit) {

    private val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tilPhoneEditProfile.setPhoneRule()
        showBackButton(R.drawable.ic_arrow_back) { navController.popBackStack() }
        etPhoneEditProfile.addCountryCodePrefix()
        saveEditProfile.onClick {
            val isNameValid = tilNameEditProfile.checkLength(getString(R.string.nameErrorAuth))
            val isPhoneValid = tilPhoneEditProfile.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
            val isEmailValid = if (tilEmailEditProfile.text().isNotEmpty()) tilEmailEditProfile.checkEmail(getString(R.string.emailErrorAuth)) else true
            if (isNameValid && isPhoneValid && isEmailValid) {
                viewModel.updateCustomerData(tilNameEditProfile.text(), tilEmailEditProfile.text())
            }
        }
        ivProfileEdit.setDebounceOnClickListener { showPhotoSourceChooserDialog() }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.customerInfoLiveData.observeExt {
            etEmailEditProfile.setText(it.email)
            etPhoneEditProfile.setText(it.phone?.addPlusSignIfNeeded())
            etNameEditProfile.setText(it.name)
        }
        viewModel.errorLiveData.observeExt { messageCallback?.showError(it) }
        viewModel.progressLiveData.observeExt { progressCallback?.setInProgress(it) }
        viewModel.avatarLiveData.observeNullableExt {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_avatar)
                .apply(RequestOptions().transform(MultiTransformation(BlurTransformation(requireContext()), CircleCrop())))
                .into(ivProfileEdit)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.onActivityResult(requestCode, resultCode, data)
    }

    private fun showPhotoSourceChooserDialog() {
        val dialog =
            ChangePhotoBottomSheetDialogFragment.newInstance(viewModel.avatarLiveData.value != null)
        dialog.clickListener = {
            when (it) {
                ChangePhotoBottomSheetDialogFragment.Button.GALLERY -> requestPickPhoto()
                ChangePhotoBottomSheetDialogFragment.Button.CAMERA -> requestTakePhoto()
                ChangePhotoBottomSheetDialogFragment.Button.DELETE -> viewModel.deleteAvatarPhoto()
            }
        }
        dialog.show(childFragmentManager, null)
    }

    private fun requestPickPhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickIntent, PICK_PHOTO_REQUEST_CODE)
    }

    private fun requestTakePhoto() {
        val isDeviceSupportCamera: Boolean = activity?.packageManager?.hasSystemFeature(
            PackageManager.FEATURE_CAMERA_ANY
        ) ?: false
        if (isDeviceSupportCamera) {
            val request = permissionsBuilder(Manifest.permission.CAMERA).build()
            request.addListener { result ->
                when {
                    result.anyPermanentlyDenied() -> openSettings()
                    result.anyShouldShowRationale() -> {
                        showAlertRes(getString(R.string.whoAreYou_cameraPermissionRationaleMessage)) {
                            cancelable = false
                            positive = R.string.common_okButton
                            positiveAction = { request.send() }
                            negative = R.string.common_closeButton
                        }
                    }
                    result.anyDenied() -> messageCallback?.showError(getString(R.string.whoAreYou_cameraPermissionDenied))
                    result.allGranted() -> {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        val uri = FileProvider.getUriForFile(
                            requireContext(),
                            "${BuildConfig.APPLICATION_ID}.fileprovider",
                            viewModel.avatarFile
                        )
                        cameraIntent.putExtra("return-data", true)
                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        startActivityForResult(cameraIntent, TAKE_PHOTO_REQUEST_CODE)
                    }
                }
            }
            request.send()
        } else {
            messageCallback?.showError(getString(R.string.whoAreYou_cameraPermissionNoCameraOnDevice))
        }
    }

    private fun openSettings() {
        showAlertRes(getString(R.string.whoAreYou_cameraPermissionPermanentlyDenied)) {
            cancelable = false
            positive = R.string.common_permissionDialog_settingsButton
            positiveAction = {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                requireContext().startActivity(intent)
            }
            negative = R.string.common_permissionDialog_cancel
        }
    }

    companion object {

        const val PICK_PHOTO_REQUEST_CODE = 9876
        const val TAKE_PHOTO_REQUEST_CODE = 6789
    }
}