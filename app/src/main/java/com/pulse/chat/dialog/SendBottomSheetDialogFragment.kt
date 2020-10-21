package com.pulse.chat.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.pulse.R
import com.pulse.core.base.fragment.BaseBottomSheetDialogFragment
import com.pulse.core.extensions.setDebounceOnClickListener
import kotlinx.android.synthetic.main.dialog_send_photo_bottom_sheet.*

class SendBottomSheetDialogFragment : BaseBottomSheetDialogFragment(R.layout.dialog_send_photo_bottom_sheet) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        send_photo_bottom_sheet_gallery.setDebounceOnClickListener {
            setFragmentResult(SEND_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to Button.GALLERY.name))
            dismiss()
        }
        send_photo_bottom_sheet_camera.setDebounceOnClickListener {
            setFragmentResult(SEND_PHOTO_KEY, bundleOf(RESULT_BUTTON_EXTRA_KEY to Button.CAMERA.name))
            dismiss()
        }
    }

    enum class Button {
        GALLERY, CAMERA
    }

    companion object {

        const val SEND_PHOTO_KEY = "SEND_PHOTO_KEY"
        const val RESULT_BUTTON_EXTRA_KEY = "RESULT_BUTTON_EXTRA_KEY"
    }
}