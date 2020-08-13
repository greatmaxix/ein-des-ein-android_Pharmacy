package com.pharmacy.myapp.chat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.onimur.handlepathoz.HandlePathOz
import br.com.onimur.handlepathoz.HandlePathOzListener
import br.com.onimur.handlepathoz.model.PathOz
import br.com.onimur.handlepathoz.utils.extension.getListUri
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pharmacy.myapp.AuthGraphArgs
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.chat.ChatFragmentDirections.Companion.actionChatToChatReviewBottomSheet
import com.pharmacy.myapp.chat.ChatFragmentDirections.Companion.actionChatToHome
import com.pharmacy.myapp.chat.ChatFragmentDirections.Companion.actionChatToSendImageBottomSheet
import com.pharmacy.myapp.chat.adapter.ChatMessageAdapter
import com.pharmacy.myapp.chat.adapter.ChatMessageAdapter.Action.*
import com.pharmacy.myapp.chat.dialog.ChatReviewBottomSheetDialogFragment.Companion.CHAT_REVIEW_FILLED
import com.pharmacy.myapp.chat.dialog.ChatReviewBottomSheetDialogFragment.Companion.CHAT_REVIEW_KEY
import com.pharmacy.myapp.chat.dialog.ChatReviewBottomSheetDialogFragment.Companion.CHAT_REVIEW_NOTE_KEY
import com.pharmacy.myapp.chat.dialog.ChatReviewBottomSheetDialogFragment.Companion.CHAT_REVIEW_RATING_KEY
import com.pharmacy.myapp.chat.dialog.SendBottomSheetDialogFragment
import com.pharmacy.myapp.chat.dialog.SendBottomSheetDialogFragment.Companion.RESULT_BUTTON_EXTRA_KEY
import com.pharmacy.myapp.chat.dialog.SendBottomSheetDialogFragment.Companion.SEND_PHOTO_KEY
import com.pharmacy.myapp.chat.model.ChatMessage
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.FlowPreview
import java.time.LocalDateTime

class ChatFragment(private val viewModel: ChatViewModel) : BaseMVVMFragment(R.layout.fragment_chat) {

    private val uri by lazy { FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", viewModel.tempPhotoFile) }

    @FlowPreview
    private val choosePhotoLauncher by lazy {
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            handlePathOz.getListRealPath(it.data.getListUri())
        }
    }
    private val takePhotoLauncher by lazy { registerForActivityResult(ActivityResultContracts.TakePicture()) { viewModel.sendPhotos(listOf(uri)) } }
    private val handlePathOz by lazy { HandlePathOz(requireContext(), listener) }
    private val listener = object : HandlePathOzListener.MultipleUri {
        override fun onRequestHandlePathOz(listPathOz: List<PathOz>, tr: Throwable?) {
            if (tr != null) {
                messageCallback?.showError(getString(R.string.sendPhotoError))
            } else {
                viewModel.sendPhotos(listPathOz.map { uri -> Uri.parse(uri.path) })
            }
        }
    }

    private val adapter = ChatMessageAdapter {
        when (it) {
            END_CHAT -> {
                hideKeyboard()
                doNav(actionChatToChatReviewBottomSheet())
            }
            RESUME_CHAT -> viewModel.removeEndChatMessage()
            AUTHORIZE -> navToAuth()
        }
    }

    private fun navToAuth() {
        val options = NavOptions.Builder()
            .setEnterAnim(R.anim.nav_enter_anim)
            .setExitAnim(R.anim.nav_exit_anim)
            .setPopEnterAnim(R.anim.nav_enter_pop_anim)
            .setPopExitAnim(R.anim.nav_exit_pop_anim)
            .setPopUpTo(R.id.nav_chat, false)
            .build()
        navController.navigate(R.id.actionChatToSignIn, AuthGraphArgs(KEY_NAVIGATION_CHAT).toBundle(), options)
    }

    private fun showChatEndDialog() {
        showAlertRes(getString(R.string.chatEndedMessage)) {
            cancelable = false
            title = R.string.chatEndedTitle
            positive = R.string.common_okButton
            positiveAction = { doNav(actionChatToHome()) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton(R.drawable.ic_arrow_back)
        initMenu(R.menu.info, Toolbar.OnMenuItemClickListener {
            if (it.itemId == R.id.menu_info) {
                // TODO menu func
                requireContext().toast("TODO: Info")
            }
            true
        })
        initAdapter()

        tilMessageChat.editText?.doAfterTextChanged {
            ivButtonSendChat.animateVisibleOrGoneIfNot(!it.isNullOrBlank())
        }
        tilMessageChat.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
            }
            false
        }
        ivButtonSendChat.setDebounceOnClickListener {
            sendMessage()
        }
        ivAttachment.setDebounceOnClickListener {
            doNav(actionChatToSendImageBottomSheet())
        }
        llMessageFieldChat.setTopRoundCornerBackground()

        attachBackPressCallback {
            doNav(actionChatToHome())
        }

        setFragmentResultListener(CHAT_REVIEW_KEY) { _, bundle ->
            if (bundle.getBoolean(CHAT_REVIEW_FILLED)) {
                val rating = bundle.getInt(CHAT_REVIEW_RATING_KEY)
                val note = bundle.getString(CHAT_REVIEW_NOTE_KEY)
                viewModel.sendReview(rating, note)
            }
            showChatEndDialog()
        }
        setFragmentResultListener(SEND_PHOTO_KEY) { _, bundle ->
            when (bundle.getString(RESULT_BUTTON_EXTRA_KEY)) {
                SendBottomSheetDialogFragment.Button.GALLERY.name -> requestPickPhoto()
                SendBottomSheetDialogFragment.Button.CAMERA.name -> requestTakePhoto()
            }
        }
    }

    private fun requestPickPhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        choosePhotoLauncher.launch(pickIntent)
    }

    private fun requestTakePhoto() {
        val isDeviceSupportCamera: Boolean = activity?.packageManager?.hasSystemFeature(
            PackageManager.FEATURE_CAMERA_ANY
        ) ?: false
        if (isDeviceSupportCamera) {
            val request = permissionsBuilder(Manifest.permission.CAMERA).build()
            request.addListener { result ->
                when {
                    result.anyPermanentlyDenied() -> openSettings()
                    result.anyShouldShowRationale() -> {
                        showAlertRes(getString(R.string.whoAreYou_cameraPermissionRationaleMessage)) {
                            cancelable = false
                            positive = R.string.common_okButton
                            positiveAction = { request.send() }
                            negative = R.string.common_closeButton
                        }
                    }
                    result.anyDenied() -> messageCallback?.showError(getString(R.string.whoAreYou_cameraPermissionDenied))
                    result.allGranted() -> takePhotoLauncher.launch(uri)
                }
            }
            request.send()
        } else {
            messageCallback?.showError(getString(R.string.whoAreYou_cameraPermissionNoCameraOnDevice))
        }
    }

    private fun openSettings() {
        showAlertRes(getString(R.string.whoAreYou_cameraPermissionPermanentlyDenied)) {
            cancelable = false
            positive = R.string.common_permissionDialog_settingsButton
            positiveAction = {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                requireContext().startActivity(intent)
            }
            negative = R.string.common_permissionDialog_cancel
        }
    }

    private fun sendMessage() {
        val message = tilMessageChat.editText?.text?.toString()?.trim().orEmpty()
        tilMessageChat.editText?.text = null
        viewModel.sendMessage(message)
    }

    override fun onBindLiveData() {
        super.onBindLiveData()

        viewModel.directionLiveData.observeExt(navController::navigate)
        viewModel.errorLiveData.observeExt { messageCallback?.showError(it) }
        viewModel.progressLiveData.observeExt { progressCallback?.setInProgress(it) }
        viewModel.isUserLoggedInLiveData.observeExt {
            if (it.not()) {
                setNotAuthorizedChatMessages()
            } else {
                llMessageFieldChat.visible()
            }
        }
        viewModel.chatMessagesLiveData.observeExt {
            adapter.setList(it)
            rvMessagesChat.postDelayed({
                rvMessagesChat.smoothScrollToPosition(0)
            }, 100)
        }
    }

    private fun setNotAuthorizedChatMessages() {
        val list = mutableListOf(
            ChatMessage.DateHeader(LocalDateTime.now()),
            ChatMessage.PharmacyMessage(getString(R.string.chat_description_message1)),
            ChatMessage.PharmacyMessage(getString(R.string.chat_description_message2)),
            ChatMessage.UserMessage(getString(R.string.chat_description_message3)),
            ChatMessage.UserMessage(getString(R.string.chat_description_message4), LocalDateTime.now()),
            ChatMessage.PharmacyMessage(getString(R.string.chat_description_message5)),
            ChatMessage.AuthorizeButton
        )
        viewModel.setNotAuthorizedChatMessages(list)
    }

    private fun initAdapter() {
        rvMessagesChat.adapter = adapter
        rvMessagesChat.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, true)
        rvMessagesChat.setHasFixedSize(true)
    }

    companion object {

        const val KEY_NAVIGATION_CHAT = "CHAT"
    }
}