package com.pharmacy.myapp.chat.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.dialog_send_photo_bottom_sheet.view.*

class SendBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_send_photo_bottom_sheet, container, false)

        view.send_photo_bottom_sheet_gallery.setDebounceOnClickListener {
            setFragmentResult(SEND_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to Button.GALLERY.name))
            dismiss()
        }
        view.send_photo_bottom_sheet_camera.setDebounceOnClickListener {
            setFragmentResult(SEND_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to Button.CAMERA.name))
            dismiss()
        }

        return view
    }

    enum class Button {
        GALLERY, CAMERA
    }

    companion object {

        const val SEND_PHOTO_KEY = "SEND_PHOTO_KEY"
        const val RESULT_BUTTON_EXTRA_KEY = "RESULT_BUTTON_EXTRA_KEY"
    }
}