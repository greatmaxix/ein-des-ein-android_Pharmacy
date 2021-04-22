package com.pulse.components.user.profile

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.pulse.R
import com.pulse.components.user.model.customer.Customer
import com.pulse.components.user.profile.ProfileFragmentDirections.Companion.actionFromProfileToSplash
import com.pulse.components.user.profile.repository.ProfileRepository
import com.pulse.core.base.mvvm.BaseViewModel
import com.pulse.core.extensions.getMultipartBody
import com.pulse.core.utils.flow.SingleShotEvent
import com.pulse.core.utils.flow.StateEventFlow
import com.pulse.util.Constants.AVATAR_FILE_NAME
import com.pulse.util.ImageFileUtil
import kotlinx.coroutines.flow.onEach
import java.io.File

class ProfileViewModel(private val context: Context, private val repository: ProfileRepository) : BaseViewModel() {

    var customer: Customer? = null
    val customerInfoFlow = repository.getCustomerInfo().onEach {
        customer = it
    }
    var avatarFile = File(context.externalCacheDir, AVATAR_FILE_NAME)
    val avatarState = StateEventFlow<String?>(avatarFile.absolutePath)
    val profileEditedEvent = SingleShotEvent<Boolean>()

    fun updateCustomerData(
        name: String = customer?.name.orEmpty(),
        email: String = customer?.email.orEmpty(),
        avatarUuid: String = customer?.avatarUuid.orEmpty()
    ) = viewModelScope.execute {
        repository.updateCustomerInfo(name, email, avatarUuid)
    }

    fun logout() = viewModelScope.execute {
        repository.logout()
        repository.clearCustomerData(customer ?: throw Exception("Customer is null"))
        deleteLocalAvatar()
        navEvent.postEvent(actionFromProfileToSplash())
    }

    fun onActivityResult(result: ActivityResult) {
        val data = result.data?.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            onActivityResult(data)
        } else {
            avatarState.postState(null)
            messageEvent.postEvent(context.getString(R.string.chooseAvatarError))
        }
    }

    fun onActivityResult(imageUri: Uri) = viewModelScope.execute {
        ImageFileUtil.saveImageByUriToFile(context, avatarFile, imageUri)
        ImageFileUtil.compressImage(context, avatarFile, imageUri)
        val uploadImage = repository.uploadImage(avatarFile.getMultipartBody("file"))
        updateCustomerData(avatarUuid = uploadImage.dataOrThrow().item.uuid)
        Glide.get(context).clearDiskCache()
        avatarState.postState(avatarFile.absolutePath)
    }

    fun deleteAvatarPhoto() {
        updateCustomerData(avatarUuid = "")
        avatarState.postState(null)
        deleteLocalAvatar()
    }

    private fun deleteLocalAvatar() {
        if (avatarFile.exists()) avatarFile.delete()
    }

    fun checkIsProfileSaved(fullName: String, email: String) {
        profileEditedEvent.offerEvent(customer?.name == fullName && customer?.email == email)
    }
}