package com.pharmacy.myapp.chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.addListener
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pharmacy.myapp.BuildConfig
import com.pharmacy.myapp.R
import com.pharmacy.myapp.auth.sign.SignInFragmentArgs
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
import com.pharmacy.myapp.chat.model.message.MessageItem
import com.pharmacy.myapp.core.base.mvvm.BaseMVVMFragment
import com.pharmacy.myapp.core.extensions.*
import com.pharmacy.myapp.data.remote.model.chat.ChatItem
import com.pharmacy.myapp.mercureService.MercureEventListenerService
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
@FlowPreview
class ChatFragment : BaseMVVMFragment(R.layout.fragment_chat) {

    private val args by navArgs<ChatFragmentArgs>()
    private val vm: ChatViewModel by viewModel(parameters = { parametersOf(args.chat) })
    private val uri by lazy { FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", vm.tempPhotoFile) }
    private val choosePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) onActivityResult(it)
    }
    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) sendPhotoMessage(uri)
    }
    private val chatAdapter = ChatMessageAdapter {
        when (it) {
            END_CHAT -> {
                observeResult<ChatItem> {
                    liveData = vm.closeChat()
                    onEmmit = {
                        hideKeyboard()
                        if (requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                            requireContext().stopService(Intent(requireContext(), MercureEventListenerService::class.java))
                        }
                        doNav(actionChatToChatReviewBottomSheet())
                    }
                }
            }
            RESUME_CHAT -> vm.removeEndChatMessage()
            AUTHORIZE -> navController.navigate(R.id.fromChatToSignIn, SignInFragmentArgs(nextDestinationId = R.id.nav_chat_type).toBundle())
        }
    }
    private var scrollerJob: Job? = null
    private val chatAdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            scrollToLastMessage()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton {
            doNav(actionChatToHome())
        }
        initMenu(R.menu.info) {
            if (it.itemId == R.id.menu_info) {
                // TODO menu func
                requireContext().toast("TODO: Info")
            }
            true
        }
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
                vm.sendReview(rating, note)
            }
            showChatEndDialog()
        }
        setFragmentResultListener(SEND_PHOTO_KEY) { _, bundle ->
            when (bundle.getString(RESULT_BUTTON_EXTRA_KEY)) {
                SendBottomSheetDialogFragment.Button.GALLERY.name -> requestPickPhoto()
                SendBottomSheetDialogFragment.Button.CAMERA.name -> requestTakePhoto()
            }
        }
        vm.checkUserLoggedIn()
    }

    override fun onResume() {
        super.onResume()

        if (args.chat != null) {
            if (!requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                val intent = Intent(requireContext(), MercureEventListenerService::class.java)
                requireContext().startService(intent)
            }
        }
    }

    private fun showChatEndDialog() {
        showAlertRes(getString(R.string.chatEndedMessage)) {
            cancelable = false
            title = R.string.chatEndedTitle
            positive = R.string.common_okButton
            positiveAction = { doNav(actionChatToHome()) }
        }
    }

    fun onActivityResult(result: ActivityResult) {
        val data = result.data?.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            sendPhotoMessage(data)
        }
    }

    private fun sendPhotoMessage(uri: Uri) {
        observeResult<MessageItem> {
            liveData = vm.sendPhoto(uri)
        }
    }

    private fun requestPickPhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                        showAlertRes(getString(R.string.cameraPermissionRationaleMessageChat)) {
                            cancelable = false
                            positive = R.string.common_okButton
                            positiveAction = { request.send() }
                            negative = R.string.common_closeButton
                        }
                    }
                    result.anyDenied() -> messageCallback?.showError(getString(R.string.cameraPermissionDenied))
                    result.allGranted() -> takePhotoLauncher.launch(uri)
                }
            }
            request.send()
        } else {
            messageCallback?.showError(getString(R.string.cameraPermissionNoCameraOnDevice))
        }
    }

    private fun openSettings() {
        showAlertRes(getString(R.string.cameraPermissionPermanentlyDenied)) {
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
        observeResult<MessageItem> {
            liveData = vm.sendMessage(message)
        }
    }

    @ExperimentalPagingApi
    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(vm.directionLiveData, navController::navigate)
        observe(vm.errorLiveData) { messageCallback?.showError(it) }
        observe(vm.progressLiveData) { progressCallback?.setInProgress(it) }
        observe(vm.isUserLoggedInLiveData) { if (it) llMessageFieldChat.visible() }
        observe(vm.chatMessagesLiveData) { chatAdapter.submitData(lifecycle, it) }
        observe(vm.lastMessageLiveData) {
            scrollToLastMessage()
        }
    }

    private fun initAdapter() {
        rvMessagesChat.adapter = chatAdapter
        rvMessagesChat.setHasFixedSize(true)
        chatAdapter.registerAdapterDataObserver(chatAdapterDataObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chatAdapter.unregisterAdapterDataObserver(chatAdapterDataObserver)
    }

    private fun scrollToLastMessage() {
        scrollerJob?.cancel()
        if (chatAdapter.itemCount != 0) {
            scrollerJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(500)
                rvMessagesChat.smoothScrollToPosition(0)
            }
        }
    }
}