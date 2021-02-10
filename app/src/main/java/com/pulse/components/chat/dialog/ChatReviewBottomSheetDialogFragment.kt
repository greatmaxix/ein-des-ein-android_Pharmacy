package com.pulse.components.chat.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.pulse.R
import com.pulse.core.base.mvvm.BaseMVVMFragmentDialogBottomSheet
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.visible
import com.pulse.data.remote.DummyData
import com.pulse.databinding.DialogChatReviewBinding

class ChatReviewBottomSheetDialogFragment : BaseMVVMFragmentDialogBottomSheet(R.layout.dialog_chat_review) {

    private val binding by viewBinding(DialogChatReviewBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        var isHighRating = false
        viewRating.setOnRatingBarChangeListener { _, rating, _ ->
            if (!mbContinue.isEnabled) mbContinue.isEnabled = true
            isHighRating = rating > 3f
            mbContinue.text = getString(if (isHighRating) R.string.sendChatReview else R.string.continueChatReview)
        }
        mbContinue.setDebounceOnClickListener {
            if (isHighRating || cgTags.isVisible) {
                setFragmentResult(
                    CHAT_REVIEW_KEY,
                    bundleOf(
                        CHAT_REVIEW_FILLED to true,
                        CHAT_REVIEW_RATING_KEY to viewRating.rating.toInt(),
                        CHAT_REVIEW_TAGS_KEY to cgTags.checkedChipIds.map { DummyData.reviewReasons[it - 1] },
                        CHAT_REVIEW_NOTE_KEY to tilReview.editText?.text?.trim()?.toString().orEmpty()
                    )
                )
                dismiss()
            } else {
                mtvTitle.text = getString(R.string.chatReviewBadDialogTitle)
                mtvDescription.text = getString(R.string.chatReviewBadDialogDescription)

                DummyData.reviewReasons.forEachIndexed { index, label ->
                    cgTags.addView(layoutInflater.inflate(R.layout.item_review_tag, null)
                        .apply {
                            with(this as Chip) {
                                text = label
                                isClickable = true
                                isCheckable = true
                                isCheckedIconVisible = false
                                id = index + 1 // because id can`t be 0
                            }
                        })
                }

                viewRating.gone()
                cgTags.visible()
                tilReview.visible()
            }
        }
        mtvSkip.setDebounceOnClickListener {
            setFragmentResult(CHAT_REVIEW_KEY, bundleOf(CHAT_REVIEW_FILLED to false))
            dismiss()
        }
    }


    companion object {

        const val CHAT_REVIEW_KEY = "CHAT_REVIEW_KEY"
        const val CHAT_REVIEW_FILLED = "CHAT_REVIEW_FILLED"
        const val CHAT_REVIEW_RATING_KEY = "CHAT_REVIEW_RATING_KEY"
        const val CHAT_REVIEW_TAGS_KEY = "CHAT_REVIEW_TAGS_KEY"
        const val CHAT_REVIEW_NOTE_KEY = "CHAT_REVIEW_NOTE_KEY"
    }
}