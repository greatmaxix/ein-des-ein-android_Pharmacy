package com.pulse.components.user.profile.edit

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragmentDialogBottomSheet
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.onClickDebounce
import com.pulse.databinding.ViewChangePhotoBottomSheetBinding

class ChangePhotoBottomSheetDialogFragment : BaseMVVMFragmentDialogBottomSheet(R.layout.view_change_photo_bottom_sheet) {

    private val binding by viewBinding(ViewChangePhotoBottomSheetBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            mtvGallery.setButtonClickListener(Button.GALLERY)
            mtvCamera.setButtonClickListener(Button.CAMERA)
            if (arguments?.getBoolean(IS_NEED_DELETE_ARGS_KEY) != true) {
                mtvDelete.gone()
            } else {
                mtvDelete.setButtonClickListener(Button.DELETE)
            }
        }
    }

    private fun View.setButtonClickListener(button: Button) {
        onClickDebounce {
            setFragmentResult(CHANGE_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to button.name))
            dismiss()
        }
    }

    companion object {

        const val CHANGE_PHOTO_KEY = "CHANGE_PHOTO_KEY"
        const val RESULT_BUTTON_EXTRA_KEY = "CHANGE_PHOTO_BUNDLE_KEY"
        const val IS_NEED_DELETE_ARGS_KEY = "isNeedDelete"
    }

    enum class Button {
        GALLERY, CAMERA, DELETE
    }
}