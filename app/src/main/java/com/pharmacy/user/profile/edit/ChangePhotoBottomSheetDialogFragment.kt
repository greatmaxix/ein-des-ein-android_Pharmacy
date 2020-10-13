package com.pharmacy.user.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pharmacy.R
import com.pharmacy.core.extensions.gone
import com.pharmacy.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.view_change_photo_bottom_sheet.view.*

class ChangePhotoBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_change_photo_bottom_sheet, container, false)

        view.change_photo_bottom_sheet_gallery.setDebounceOnClickListener {
            setFragmentResult(CHANGE_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to Button.GALLERY.name))
            dismiss()
        }
        view.change_photo_bottom_sheet_camera.setDebounceOnClickListener {
            setFragmentResult(CHANGE_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to Button.CAMERA.name))
            dismiss()
        }
        if (arguments?.getBoolean(IS_NEED_DELETE_ARGS_KEY) != true) {
            view.change_photo_bottom_sheet_delete.gone()
        } else {
            view.change_photo_bottom_sheet_delete.setDebounceOnClickListener {
                setFragmentResult(CHANGE_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to Button.DELETE.name))
                dismiss()
            }
        }

        return view
    }

    enum class Button {
        GALLERY, CAMERA, DELETE
    }

    companion object {

        const val CHANGE_PHOTO_KEY = "CHANGE_PHOTO_KEY"
        const val RESULT_BUTTON_EXTRA_KEY = "CHANGE_PHOTO_BUNDLE_KEY"
        const val IS_NEED_DELETE_ARGS_KEY = "isNeedDelete"
    }
}