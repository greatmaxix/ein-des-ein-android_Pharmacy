package com.pharmacy.core.general.behavior

interface IProgressBehavior: IBehavior {

    fun showProgress()

    fun setInProgress(progress: Boolean) {
        if (progress) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    fun hideProgress()

}