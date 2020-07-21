package com.pharmacy.myapp.qrCodeScanner

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pharmacy.myapp.R

class SearchResultBottomSheet : BottomSheetDialogFragment() {

    private var onDismiss: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_search_result, container)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss.invoke()
    }

    companion object {

        val TAG = "${SearchResultBottomSheet::class.java.canonicalName}_TAG"

        fun newInstance(onDismiss: () -> Unit): SearchResultBottomSheet =
            SearchResultBottomSheet().apply {
                this.onDismiss = onDismiss
            }
    }
}