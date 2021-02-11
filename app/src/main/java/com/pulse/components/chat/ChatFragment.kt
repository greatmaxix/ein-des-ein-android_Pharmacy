package com.pulse.components.chat

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
import by.kirich1409.viewbindingdelegate.viewBinding
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.anyDenied
import com.fondesa.kpermissions.anyPermanentlyDenied
import com.fondesa.kpermissions.anyShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.pulse.BuildConfig
import com.pulse.R
import com.pulse.components.auth.sign.SignInFragmentArgs
import com.pulse.components.chat.ChatFragmentDirections.Companion.actionChatToChatReviewBottomSheet
import com.pulse.components.chat.ChatFragmentDirections.Companion.actionChatToHome
import com.pulse.components.chat.ChatFragmentDirections.Companion.actionChatToSendImageBottomSheet
import com.pulse.components.chat.ChatFragmentDirections.Companion.fromChatToProductCard
import com.pulse.components.chat.adapter.ChatMessageAdapter
import com.pulse.components.chat.adapter.ChatMessageAdapter.Action.*
import com.pulse.components.chat.adapter.viewHolder.ProductViewHolder
import com.pulse.components.chat.adapter.viewHolder.ProductViewHolder.Action.ITEM
import com.pulse.components.chat.adapter.viewHolder.ProductViewHolder.Action.WISH
import com.pulse.components.chat.dialog.ChatReviewBottomSheetDialogFragment.Companion.CHAT_REVIEW_FILLED
import com.pulse.components.chat.dialog.ChatReviewBottomSheetDialogFragment.Companion.CHAT_REVIEW_KEY
import com.pulse.components.chat.dialog.ChatReviewBottomSheetDialogFragment.Companion.CHAT_REVIEW_NOTE_KEY
import com.pulse.components.chat.dialog.ChatReviewBottomSheetDialogFragment.Companion.CHAT_REVIEW_RATING_KEY
import com.pulse.components.chat.dialog.ChatReviewBottomSheetDialogFragment.Companion.CHAT_REVIEW_TAGS_KEY
import com.pulse.components.chat.dialog.SendBottomSheetDialogFragment
import com.pulse.components.chat.dialog.SendBottomSheetDialogFragment.Companion.RESULT_BUTTON_EXTRA_KEY
import com.pulse.components.chat.dialog.SendBottomSheetDialogFragment.Companion.SEND_PHOTO_KEY
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.components.mercureService.MercureEventListenerService
import com.pulse.core.base.mvvm.BaseMVVMFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentChatBinding
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
    private val binding by viewBinding(FragmentChatBinding::bind)
    private val viewModel: ChatViewModel by viewModel(parameters = { parametersOf(args.chat) })
    private val uri by lazy { FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", viewModel.tempPhotoFile) }
    private val choosePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { if (it.resultCode == Activity.RESULT_OK) onActivityResult(it) }
    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { if (it) sendPhotoMessage(uri) }
    private val chatAdapter = ChatMessageAdapter(
        { action ->
            when (action) {
                END_CHAT -> {
                    observeResult(viewModel.closeChat()) {
                        onEmmit = {
                            hideKeyboard()
                            if (requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                                requireContext().stopService(Intent(requireContext(), MercureEventListenerService::class.java))
                            }
                            doNav(actionChatToChatReviewBottomSheet())
                        }
                    }
                }
                RESUME_CHAT -> observeResult(viewModel.resumeChat())
                AUTHORIZE -> navController.navigate(R.id.fromChatToSignIn, SignInFragmentArgs(nextDestinationId = R.id.nav_chat_type).toBundle())
            }
        },
        { action: ProductViewHolder.Action, pair: Pair<MessageItem, Int> ->
            when (action) {
                ITEM -> viewModel.getProductInfo(pair.second)
                WISH -> viewModel.setOrRemoveWish(pair)
            }
        })

    private var scrollerJob: Job? = null
    private val chatAdapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            scrollToLastMessage()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        showBackButton { doNav(actionChatToHome()) }
        initMenu(R.menu.info) {
            if (it.itemId == R.id.menu_info) {
                // TODO menu func
                requireContext().toast("TODO: Info")
            }
            true
        }
        initAdapter()

        tilMessage.editText?.doAfterTextChanged {
            ivSend.animateVisibleOrGoneIfNot(!it.isNullOrBlank())
        }
        tilMessage.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
            }
            false
        }
        ivSend.setDebounceOnClickListener {
            sendMessage()
        }
        ivAttachment.setDebounceOnClickListener {
            doNav(actionChatToSendImageBottomSheet())
        }
        llMessageField.setTopRoundCornerBackground()

        attachBackPressCallback {
            doNav(actionChatToHome())
        }

        setFragmentResultListener(CHAT_REVIEW_KEY) { _, bundle ->
            if (bundle.getBoolean(CHAT_REVIEW_FILLED)) {
                val rating = bundle.getInt(CHAT_REVIEW_RATING_KEY)
                val tags = bundle.getStringArrayList(CHAT_REVIEW_TAGS_KEY)
                val note = bundle.getString(CHAT_REVIEW_NOTE_KEY)
                observeResult(viewModel.sendReview(rating, tags, note)) {
                    onEmmit = { showChatEndDialog() }
                }
            } else {
                showChatEndDialog()
            }
        }
        setFragmentResultListener(SEND_PHOTO_KEY) { _, bundle ->
            when (bundle.getString(RESULT_BUTTON_EXTRA_KEY)) {
                SendBottomSheetDialogFragment.Button.GALLERY.name -> requestPickPhoto()
                SendBottomSheetDialogFragment.Button.CAMERA.name -> requestTakePhoto()
            }
        }
        viewModel.checkUserLoggedIn()
    }

    override fun onResume() {
        super.onResume()

        if (args.chat != null) {
            if (!requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                val intent = Intent(requireContext(), MercureEventListenerService::class.java)
                requireContext().startService(intent)
            }
        }
        viewModel.checkIsWishSaved()
    }

    private fun showChatEndDialog(isAutoClosed: Boolean = false) {
        showAlertRes(getString(if (isAutoClosed) R.string.chat_ended_automatically_message else R.string.chatEndedMessage)) {
            cancelable = false
            title = R.string.chatEndedTitle
            positive = R.string.common_okButton
            positiveAction = { doNav(actionChatToHome()) }
        }
    }

    private fun onActivityResult(result: ActivityResult) {
        val data = result.data?.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            sendPhotoMessage(data)
        }
    }

    private fun sendPhotoMessage(uri: Uri) {
        observeResult(viewModel.sendPhoto(uri))
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
                            negative = R.string.cancel
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
        val message = binding.tilMessage.editText?.text?.toString()?.trim().orEmpty()
        binding.tilMessage.editText?.text = null
        observeResult(viewModel.sendMessage(message))
    }

    @ExperimentalPagingApi
    override fun onBindLiveData() {
        super.onBindLiveData()

        observe(viewModel.directionLiveData, navController::navigate)
        observe(viewModel.isUserLoggedInLiveData) { if (it) binding.llMessageField.visible() }
        observe(viewModel.chatMessagesLiveData) { chatAdapter.submitData(lifecycle, it) }
        observe(viewModel.lastMessageLiveData) { scrollToLastMessage() }
        observe(viewModel.chatLiveData) { if (it?.isAutomaticClosed.falseIfNull()) showChatEndDialog(true) }
        observe(viewModel.productLiteLiveData) { navController.navigate(fromChatToProductCard(it)) }
        observe(viewModel.wishLiteLiveData) { chatAdapter.notifyWish(it) }
    }

    private fun initAdapter() = with(binding.rvMessages) {
        adapter = chatAdapter
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
                binding.rvMessages.smoothScrollToPosition(0)
            }
        }
    }
}