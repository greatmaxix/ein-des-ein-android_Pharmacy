package com.pulse.chat.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.pulse.R
import com.pulse.core.extensions.gone
import com.pulse.core.extensions.setDebounceOnClickListener
import com.pulse.core.extensions.visible
import com.pulse.data.remote.DummyData
import kotlinx.android.synthetic.main.dialog_chat_review.view.*

class ChatReviewBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_chat_review, container, false)

        var isHighRating = false
        view.ratingChatReview.setOnRatingBarChangeListener { _, rating, _ ->
            if (!view.btnContinueReview.isEnabled) view.btnContinueReview.isEnabled = true
            isHighRating = rating > 3f
            view.btnContinueReview.text = getString(if (isHighRating) R.string.sendChatReview else R.string.continueChatReview)
        }
        view.btnContinueReview.setDebounceOnClickListener {
            if (isHighRating || view.chipGroupReviewReason.isVisible) {
                setFragmentResult(
                    CHAT_REVIEW_KEY,
                    bundleOf(
                        CHAT_REVIEW_FILLED to true,
                        CHAT_REVIEW_RATING_KEY to view.ratingChatReview.rating.toInt(),
                        CHAT_REVIEW_NOTE_KEY to view.tilChatReview.editText?.text?.trim()?.toString().orEmpty()
                    )
                )
                dismiss()
            } else {
                view.tvChatReviewTitle.text = getString(R.string.chatReviewBadDialogTitle)
                view.tvChatReviewDescription.text = getString(R.string.chatReviewBadDialogDescription)

                DummyData.getReviewReasons().forEachIndexed { index, label ->
                    view.chipGroupReviewReason.addView(inflater.inflate(R.layout.item_review_tag, null)
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

                view.ratingChatReview.gone()
                view.chipGroupReviewReason.visible()
                view.tilChatReview.visible()
            }
        }
        view.tvSkipChatReview.setDebounceOnClickListener {
            setFragmentResult(
                CHAT_REVIEW_KEY,
                bundleOf(CHAT_REVIEW_FILLED to false)
            )
            dismiss()
        }

        return view
    }

    companion object {

        const val CHAT_REVIEW_KEY = "CHAT_REVIEW_KEY"

        const val CHAT_REVIEW_FILLED = "CHAT_REVIEW_FILLED"
        const val CHAT_REVIEW_RATING_KEY = "CHAT_REVIEW_RATING_KEY"
        const val CHAT_REVIEW_NOTE_KEY = "CHAT_REVIEW_NOTE_KEY"
    }
}