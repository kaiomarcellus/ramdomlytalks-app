package br.com.kmdev.randomlytalksapp.ui.conversation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.kmdev.randomlytalksapp.R
import br.com.kmdev.randomlytalksapp.databinding.FragmentConversationBinding
import br.com.kmdev.ui.components.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConversationFragment : Fragment() {

    private lateinit var binding: FragmentConversationBinding
    private var messagesAdapter: ConversationListAdapter? = null
    private val viewModel: ConversationViewModel by viewModel()
    private val roomId: String by lazy { arguments?.getString("RoomId") ?: "" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentConversationBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (roomId.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.conversation_label_attention))
                .setMessage(getString(R.string.conversation_label_unavailable_room))
                .setOnDismissListener { findNavController().popBackStack() }
                .create().show()
            return
        }
        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.apply {

            tvConversationRoomName.text = roomId

            messagesAdapter = ConversationListAdapter()
            rvConversationMessages.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = messagesAdapter
            }
            inputConversationMessage.imeOptionsCallback = {
                inputConversationMessage.hideKeyboard()
                sendMessage(inputConversationMessage.getContentText())
            }
            fabConversationSend.setOnClickListener {
                sendMessage(inputConversationMessage.getContentText())
            }
        }
    }

    private fun setupObservers() {
        binding.apply {

            viewModel.errorMessage.observe(viewLifecycleOwner) {

            }
            viewModel.roomMessagesData.observe(viewLifecycleOwner) {
                messagesAdapter?.addItem(it)
                messagesAdapter?.itemCount?.let { count ->
                    rvConversationMessages.scrollToPosition(count - 1)
                }
            }
            viewModel.connected.observe(viewLifecycleOwner) {
                if (it) {
                    viewModel.joinAtRoom(roomId)
                    viewModel.fetchMessages(roomId)
                }
            }
            viewModel.userName.observe(viewLifecycleOwner) {
                viewModel.setupConnection()
            }

            viewModel.fetchUsername()
        }
    }

    private fun sendMessage(contentText: String) {
        if (contentText.isNotEmpty()) {
            binding.inputConversationMessage.clear()
            viewModel.sendMessage(roomId, contentText)
        }
    }
}