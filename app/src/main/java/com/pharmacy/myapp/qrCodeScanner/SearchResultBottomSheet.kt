package com.pharmacy.myapp.qrCodeScanner

import android.content.DialogInterface
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragmentDialogBottomSheet

class SearchResultBottomSheet : BaseMVVMFragmentDialogBottomSheet(R.layout.dialog_search_result) {

    private var onDismiss: () -> Unit = {}

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss.invoke()
    }

    companion object {

        fun newInstance(onDismiss: () -> Unit): SearchResultBottomSheet =
            SearchResultBottomSheet().apply {
                this.onDismiss = onDismiss
            }
    }
}