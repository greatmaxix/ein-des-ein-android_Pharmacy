package com.pharmacy.myapp.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.gone
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.view_change_photo_bottom_sheet.view.*

class ChangePhotoBottomSheetDialogFragment : BottomSheetDialogFragment() {

    var clickListener: ((Button) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_change_photo_bottom_sheet, container, false)

        view.change_photo_bottom_sheet_gallery.setDebounceOnClickListener {
            clickListener?.invoke(Button.GALLERY)
            dismiss()
        }
        view.change_photo_bottom_sheet_camera.setDebounceOnClickListener {
            clickListener?.invoke(Button.CAMERA)
            dismiss()
        }
        if (arguments?.getBoolean(ARG_WITH_DELETE) != true) {
            view.change_photo_bottom_sheet_delete.gone()
            view.change_photo_bottom_sheet_delete.gone()
        } else {
            view.change_photo_bottom_sheet_delete.setOnClickListener {
                clickListener?.invoke(Button.DELETE)
                dismiss()
            }
        }

        return view
    }

    enum class Button {
        GALLERY,
        CAMERA,
        DELETE
    }

    companion object {

        private const val ARG_WITH_DELETE = "with_delete"

        fun newInstance(withDelete: Boolean) = ChangePhotoBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_WITH_DELETE, withDelete)
            }
        }
    }
}