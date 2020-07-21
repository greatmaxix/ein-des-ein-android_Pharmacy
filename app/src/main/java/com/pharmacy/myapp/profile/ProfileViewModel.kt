package com.pharmacy.myapp.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.pharmacy.myapp.R
import com.pharmacy.myapp.core.base.mvvm.BaseViewModel
import com.pharmacy.myapp.core.general.SingleLiveEvent
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.profile.ProfileFragmentDirections.Companion.actionFromProfileToSplash
import com.pharmacy.myapp.profile.edit.EditProfileFragment.Companion.TAKE_PHOTO_REQUEST_CODE
import com.pharmacy.myapp.util.ImageFileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File

class ProfileViewModel(private val context: Context,
                       private val repository: ProfileRepository) : BaseViewModel() {

    val errorLiveData by lazy { SingleLiveEvent<String>() }
    val progressLiveData by lazy { SingleLiveEvent<Boolean>() }
    val userDataLiveData by lazy { SingleLiveEvent<Triple<String?, String?, String?>>() }
    val directionLiveData by lazy { SingleLiveEvent<NavDirections>() }
    var avatarFile = File(context.externalCacheDir, AVATAR_FILE_NAME)
    private val _avatarLiveData = MutableLiveData<String?>(avatarFile.absolutePath)
    val avatarLiveData: LiveData<String?> = _avatarLiveData

    fun getUserData() = userDataLiveData.postValue(repository.getUserData())

    fun updateCustomerData(name: String, email: String) = launchIO {
        progressLiveData.postValue(true)
        val response = repository.updateCustomerData(name, email)
        progressLiveData.postValue(false)
        when (response) {
            is Success -> repository.saveNewUserData(response.value)
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
                directionLiveData.postValue(actionFromProfileToSplash())
            }
            is Error -> errorLiveData.postValue(response.errorMessage)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val imageUri = if (requestCode == TAKE_PHOTO_REQUEST_CODE) Uri.fromFile(avatarFile) else data?.data
        if (resultCode == Activity.RESULT_OK && imageUri != null) {
            progressLiveData.postValue(true)
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    ImageFileUtil.saveImageByUriToFile(context, avatarFile, imageUri)
                    ImageFileUtil.compressImage(context, avatarFile, imageUri)
                    /*val contentType = context.contentResolver.getType(imageUri)?.toMediaTypeOrNull()
                    val asRequestBody = avatarFile.asRequestBody(contentType)
                    val createFormData = MultipartBody.Part.createFormData("image", avatarFile.name, asRequestBody)
                    val uploadImageResponse = repository.uploadImage(createFormData)
//
                    when (uploadImageResponse) {
                        is Success -> Timber.d(uploadImageResponse.value.toString())
                        is Error -> Timber.d(uploadImageResponse.errorMessage)
                    }*/
                    val partMap = HashMap<String, RequestBody>()

                    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(avatarFile.absolutePath)
                    val fileType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)?:""

                    partMap["image\"; filename=\"${avatarFile.name}\""] =
                        avatarFile.asRequestBody(fileType.toMediaTypeOrNull())
                    val uploadImage = repository.uploadImage(partMap)
                    when (uploadImage) {
                        is Success -> Timber.d(uploadImage.value.toString())
                        is Error -> Timber.d(uploadImage.errorMessage)
                    }

                }
                _avatarLiveData.value = avatarFile.absolutePath
                progressLiveData.postValue(false)
            }
        } else {
            _avatarLiveData.value = null
            errorLiveData.postValue(context.getString(R.string.whoAreYou_errorSavingAvatar))
        }
    }

    fun deleteAvatarPhoto() {
        _avatarLiveData.postValue(null)
        if (avatarFile.exists()) {
            avatarFile.delete()
        }
    }

    companion object {
        private const val AVATAR_FILE_NAME = "user_avatar.jpg"
    }
}