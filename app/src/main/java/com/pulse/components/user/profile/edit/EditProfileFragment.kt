package com.pulse.components.user.profile.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pulse.BuildConfig
import com.pulse.R
import com.pulse.components.user.profile.ProfileViewModel
import com.pulse.components.user.profile.edit.ChangePhotoBottomSheetDialogFragment.Button
import com.pulse.components.user.profile.edit.ChangePhotoBottomSheetDialogFragment.Companion.CHANGE_PHOTO_KEY
import com.pulse.components.user.profile.edit.ChangePhotoBottomSheetDialogFragment.Companion.RESULT_BUTTON_EXTRA_KEY
import com.pulse.components.user.profile.edit.EditProfileFragmentDirections.Companion.actionFromProfileEditToChangePhoto
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentProfileEditBinding
import com.pulse.ui.text.checkEmail
import com.pulse.ui.text.checkLength
import com.pulse.ui.text.isPhoneNumberValid
import com.pulse.ui.text.setPhoneRule
import com.pulse.util.BlurTransformation

class EditProfileFragment : BaseMVVMFragment(R.layout.fragment_profile_edit) {

    private val binding by viewBinding(FragmentProfileEditBinding::bind)
    private val viewModel: ProfileViewModel by sharedGraphViewModel(R.id.profile_graph)
    private val uri by lazy { FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", viewModel.avatarFile) }
    private lateinit var choosePhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>
    private var userDataChanged = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton()
        tilPhone.setPhoneRule()
        mbSave.setDebounceOnClickListener {
            val isNameValid = tilName.checkLength(getString(R.string.nameErrorAuth))
            val isPhoneValid = tilPhone.isPhoneNumberValid(getString(R.string.phoneErrorAuth))
            val isEmailValid = if (tilEmail.text().isNotEmpty()) tilEmail.checkEmail(getString(R.string.emailErrorAuth)) else true
            if (isNameValid && isPhoneValid && isEmailValid) {
                viewModel.updateCustomerData(tilName.text(), tilEmail.text())
                userDataChanged = false
            }
        }
        ivProfile.setDebounceOnClickListener { showPhotoSourceChooserDialog() }
        setFragmentResultListener(CHANGE_PHOTO_KEY) { _, bundle -> handleChangePhotoAction(bundle) }
        attachBackPressCallback { backPressedHandler() }

        choosePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), viewModel::onActivityResult)
        takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { viewModel.onActivityResult(uri) }
    }

    private fun backPressedHandler() {
        if (userDataChanged) {
            showAlertRes(getString(R.string.exitWithoutSaving)) {
                cancelable = false
                positive = R.string.common_okButton
                positiveAction = { navController.popBackStack() }
                negative = R.string.cancel
            }
        } else {
            navController.popBackStack()
        }
    }

    override fun onBindLiveData() = with(binding) {
        super.onBindLiveData()
        observe(viewModel.customerInfoLiveData) {
            etEmail.setText(it?.email)
            etPhone.setText(it?.phone?.addPlusSignIfNeeded())
            etName.setText(it?.name)
            etName.addTextChangedListener { userDataChanged = true }
            etEmail.addTextChangedListener { userDataChanged = true }
        }
        observe(viewModel.errorLiveData) { messageCallback?.showError(it) }
        observe(viewModel.progressLiveData) { progressCallback?.setInProgress(it) }
        observeNullable(viewModel.avatarLiveData) {
            ivProfile.loadGlideDrawableByURL(it) {
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
                            negative = R.string.cancel
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