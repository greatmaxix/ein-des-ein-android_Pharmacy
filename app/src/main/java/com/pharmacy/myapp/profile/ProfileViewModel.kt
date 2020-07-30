package com.pharmacy.myapp.profile

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.bumptech.glide.Glide
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.extensions.getMultipartBody
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.model.customerInfo.CustomerInfo
import com.pharmacy.myapp.profile.ProfileFragmentDirections.Companion.actionFromProfileToSplash
import com.pharmacy.myapp.util.Constants.Companion.AVATAR_FILE_NAME
import com.pharmacy.myapp.util.ImageFileUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class ProfileViewModel(private val context: Context, private val repository: ProfileRepository) :BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val customerInfoLiveData = repository.getCustomerInfo()
    val directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    var avatarFile = File(context.externalCacheDir, AVATAR_FILE_NAME)
    private val _avatarLiveData = MutableLiveData<String?>(avatarFile.absolutePath)
    val avatarLiveData: LiveData<String?> = _avatarLiveData

    fun updateCustomerData(
        name: String = customerInfoLiveData.value?.name.orEmpty(),
        email: String = customerInfoLiveData.value?.email.orEmpty(),
        avatarUuid: String = customerInfoLiveData.value?.avatarUuid.orEmpty()
    ) = launchIO {
        progressLiveData.postValue(true)
        val response = repository.updateCustomerInfo(name, email, avatarUuid)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {}/*_customerInfoLiveData.postValue(repository.getCustomerInfo())*/
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

    fun logout() = launchIO {
        progressLiveData.postValue(true)
        val response = repository.logout()
        progressLiveData.postValue(false)
        when (response) {
            is Success -> {
                repository.clearCustomerData()
                deleteAvatarPhoto()
                directionLiveData.postValue(actionFromProfileToSplash())
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

    fun onActivityResult(result: ActivityResult) {
        val data = result.data?.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            onActivityResult(data)
        } else {
            _avatarLiveData.value = null
            errorLiveData.postValue(context.getString(R.string.chooseAvatarError))
        }
    }

    fun onActivityResult(imageUri: Uri) {
        progressLiveData.postValue(true)
        viewModelScope.launch {
            withContext(IO) {
                ImageFileUtil.saveImageByUriToFile(context, avatarFile, imageUri)
                ImageFileUtil.compressImage(context, avatarFile, imageUri)

                when (val uploadImage = repository.uploadImage(avatarFile.getMultipartBody())) {
                    is Success -> {
                        updateCustomerData(avatarUuid = uploadImage.value.data.uuid)
                        Glide.get(context).clearDiskCache()
                        _avatarLiveData.postValue(avatarFile.absolutePath)
                    }
                    is Error -> errorLiveData.postValue(uploadImage.errorMessage)
                }
            }
            progressLiveData.postValue(false)
        }
    }

    fun deleteAvatarPhoto() {
        updateCustomerData(avatarUuid = "")
        _avatarLiveData.postValue(null)
        if (avatarFile.exists()) {
            avatarFile.delete()
        }
    }

}