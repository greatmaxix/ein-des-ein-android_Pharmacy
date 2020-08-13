package com.pharmacy.myapp.profile.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
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
import com.pharmacy.myapp.profile.edit.ChangePhotoBottomSheetDialogFragment.Button
import com.pharmacy.myapp.profile.edit.ChangePhotoBottomSheetDialogFragment.Companion.CHANGE_PHOTO_KEY
import com.pharmacy.myapp.profile.edit.ChangePhotoBottomSheetDialogFragment.Companion.RESULT_BUTTON_EXTRA_KEY
import com.pharmacy.myapp.profile.edit.EditProfileFragmentDirections.Companion.actionFromProfileEditToChangePhoto
import com.pharmacy.myapp.ui.text.*
import com.pharmacy.myapp.util.BlurTransformation
import kotlinx.android.synthetic.main.fragment_profile_edit.*

class EditProfileFragment : BaseMVVMFragment(R.layout.fragment_profile_edit) {

    private val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)

    private val uri by lazy { FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", viewModel.avatarFile) }

    private val choosePhotoLauncher by lazy { registerForActivityResult(ActivityResultContracts.StartActivityForResult(), viewModel::onActivityResult) }
    private val takePhotoLauncher by lazy {
        registerForActivityResult(ActivityResultContracts.TakePicture()) { viewModel.onActivityResult(uri) }
    }
    private var userDataChanged = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tilPhoneEditProfile.setPhoneRule()
        showBackButton(R.drawable.ic_arrow_back) { backPressedHandler() }
        etPhoneEditProfile.addCountryCodePrefix()
        saveEditProfile.onClick {
            val isNameValid = tilNameEditProfile.checkLength(getString(R.string.nameErrorAuth))
            val isPhoneValid = tilPhoneEditProfile.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
            val isEmailValid = if (tilEmailEditProfile.text().isNotEmpty()) tilEmailEditProfile.checkEmail(getString(R.string.emailErrorAuth)) else true
            if (isNameValid && isPhoneValid && isEmailValid) {
                viewModel.updateCustomerData(tilNameEditProfile.text(), tilEmailEditProfile.text())
                userDataChanged = false
            }
        }
        ivProfileEdit.setDebounceOnClickListener { showPhotoSourceChooserDialog() }
        setFragmentResultListener(CHANGE_PHOTO_KEY) { _, bundle -> handleChangePhotoAction(bundle) }
        attachBackPressCallback { backPressedHandler() }
    }

    private fun backPressedHandler() {
        if (userDataChanged) {
            showAlertRes(getString(R.string.exitWithoutSaving)) {
                cancelable = false
                positive = R.string.common_okButton
                positiveAction = { navController.popBackStack() }
                negative = R.string.common_closeButton
            }
        } else {
            navController.popBackStack()
        }
    }

    override fun onBindLiveData() {
        super.onBindLiveData()
        viewModel.customerInfoLiveData.observeExt {
            etEmailEditProfile.setText(it.email)
            etPhoneEditProfile.setText(it.phone.addPlusSignIfNeeded())
            etNameEditProfile.setText(it.name)

            etNameEditProfile.addTextChangedListener { userDataChanged = true }
            etEmailEditProfile.addTextChangedListener { userDataChanged = true }
        }
        viewModel.errorLiveData.observeExt { messageCallback?.showError(it) }
        viewModel.progressLiveData.observeExt { progressCallback?.setInProgress(it) }
        viewModel.avatarLiveData.observeNullableExt {
            ivProfileEdit.loadGlide(it) {
                placeholder(R.drawable.ic_avatar)
                skipMemoryCache(true)
                apply(RequestOptions().transform(MultiTransformation(BlurTransformation(requireContext()), CircleCrop())))
            }
        }
    }

    private fun showPhotoSourceChooserDialog() =
        doNav(actionFromProfileEditToChangePhoto(viewModel.avatarLiveData.value != null))

    private fun handleChangePhotoAction(bundle: Bundle) {
        when (bundle.getString(RESULT_BUTTON_EXTRA_KEY)) {
            Button.GALLERY.name -> requestPickPhoto()
            Button.CAMERA.name -> requestTakePhoto()
            Button.DELETE.name -> viewModel.deleteAvatarPhoto()
        }
    }

    private fun requestPickPhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        choosePhotoLauncher.launch(pickIntent)
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
                        showAlertRes(getString(R.string.cameraPermissionRationaleMessage)) {
                            cancelable = false
                            positive = R.string.common_okButton
                            positiveAction = { request.send() }
                            negative = R.string.common_closeButton
                        }
                    }
                    result.anyDenied() -> messageCallback?.showError(getString(R.string.cameraPermissionDenied))
                    result.allGranted() -> takePhotoLauncher.launch(uri)
                }
            }
            request.send()
        } else {
            messageCallback?.showError(getString(R.string.cameraPermissionNoCameraOnDevice))
        }
    }

    private fun openSettings() {
        showAlertRes(getString(R.string.cameraPermissionPermanentlyDenied)) {
            cancelable = false
            positive = R.string.common_permissionDialog_settingsButton
            positiveAction = {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                startActivity(intent)
            }
            negative = R.string.common_permissionDialog_cancel
        }
    }

}