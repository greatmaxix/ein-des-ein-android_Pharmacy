package com.pulse.components.chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
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
import com.pulse.core.base.fragment.BaseToolbarFragment
import com.pulse.core.extensions.*
import com.pulse.databinding.FragmentChatBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.parameter.parametersOf

@KoinApiExtension
class ChatFragment : BaseToolbarFragment<ChatViewModel>(R.layout.fragment_chat, ChatViewModel::class, R.menu.info) {

    private val args by navArgs<ChatFragmentArgs>()
    private val binding by viewBinding(FragmentChatBinding::bind)
    override val viewModel: ChatViewModel by viewModel(parameters = { parametersOf(args.chat) })
    private val uri by lazy { FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", viewModel.tempPhotoFile) }
    private val choosePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { if (it.resultCode == Activity.RESULT_OK) onActivityResult(it) }
    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { if (it) sendPhotoMessage(uri) }
    private val chatAdapter = ChatMessageAdapter(
        { action ->
            when (action) {
                END_CHAT -> viewModel.closeChat()
                RESUME_CHAT -> viewModel.resumeChat()
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

    override fun initUI() = with(binding) {
        showBackButton()
        initAdapter()

        tilMessage.editText?.doAfterTextChanged { ivSend.animateVisibleOrGoneIfNot(!it.isNullOrBlank()) }
        tilMessage.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
            }
            false
        }
        ivSend.setDebounceOnClickListener { sendMessage() }
        ivAttachment.setDebounceOnClickListener { navController.navigate(actionChatToSendImageBottomSheet()) }
        llMessageField.setTopRoundCornerBackground()

        attachBackPressCallback { navController.navigate(actionChatToHome()) }

        setFragmentResultListener(CHAT_REVIEW_KEY) { _, bundle ->
            if (bundle.getBoolean(CHAT_REVIEW_FILLED)) {
                val rating = bundle.getInt(CHAT_REVIEW_RATING_KEY)
                val tags = bundle.getStringArrayList(CHAT_REVIEW_TAGS_KEY)
                val note = bundle.getString(CHAT_REVIEW_NOTE_KEY)
                viewModel.sendReview(rating, tags, note)
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
    }

    override fun onClickNavigation() = navController.navigate(actionChatToHome())

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
        hideKeyboard()
        uiHelper.showDialog(getString(if (isAutoClosed) R.string.chat_ended_automatically_message else R.string.chatEndedMessage)) {
            cancelable = false
            title = getString(R.string.chatEndedTitle)
            positive = getString(R.string.common_okButton)
            positiveAction = { navController.navigate(actionChatToHome()) }
        }
    }

    private fun onActivityResult(result: ActivityResult) {
        val data = result.data?.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            sendPhotoMessage(data)
        }
    }

    private fun sendPhotoMessage(uri: Uri) = viewModel.sendPhoto(uri)

    private fun requestPickPhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        choosePhotoLauncher.launch(pickIntent)
    }

    private fun requestTakePhoto() {
        val isDeviceSupportCamera: Boolean = activity?.packageManager?.hasSystemFeature(
            PackageManager.FEATURE_CAMERA_ANY
        ) ?: false
        if (isDeviceSupportCamera) {
            requestPermissions(
                firstPermission = Manifest.permission.CAMERA,
                openSettingsMessage = R.string.cameraPermissionPermanentlyDenied,
                rationaleMessage = R.string.cameraPermissionRationaleMessage,
                deniedMessage = R.string.cameraPermissionDenied
            ) { takePhotoLauncher.launch(uri) }
        } else {
            uiHelper.showMessage(getString(R.string.cameraPermissionNoCameraOnDevice))
        }
    }

    private fun sendMessage() {
        val message = binding.tilMessage.editText?.text?.toString()?.trim().orEmpty()
        binding.tilMessage.editText?.text = null
        viewModel.sendMessage(message)
    }

    override fun onBindEvents() = with(lifecycleScope) {
        observe(menuItemsFlow) {
            if (it.itemId == R.id.menu_info) {
                // TODO menu func
                requireContext().toast("TODO: Info")
            }
        }
        observe(viewModel.closeChatResultEvent.events) {
            hideKeyboard()
            if (requireContext().isServiceRunning(MercureEventListenerService::class.java)) {
                requireContext().stopService(Intent(requireContext(), MercureEventListenerService::class.java))
            }
            navController.navigate(actionChatToChatReviewBottomSheet())
        }
        observe(viewModel.endChatResultEvent.events) { showChatEndDialog() }
        observe(viewModel.wishEvent.events) { chatAdapter.notifyWish(it) }
    }

    @ExperimentalPagingApi
    override fun onBindStates() = with(lifecycleScope) {
        observe(viewModel.inputFieldState) {
            binding.llMessageField.visibleOrGone(it)
            if (!it) hideKeyboard()
        }
        observe(viewModel.chatMessagesState) { chatAdapter.submitData(lifecycle, it) }
        observe(viewModel.lastMessageState) { scrollToLastMessage() }
        observe(viewModel.chatFlow) { if (it?.isAutomaticClosed.falseIfNull()) showChatEndDialog(true) }
        observe(viewModel.productLiteState) { it?.let { navController.navigate(fromChatToProductCard(it)) } }
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
                binding.rvMessages.smoothScrollToPosition(0)
            }
        }
    }
}