package br.com.kmdev.randomlytalksapp.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.kmdev.randomlytalksapp.R
import br.com.kmdev.randomlytalksapp.data.viewdata.RoomItemDTO
import br.com.kmdev.randomlytalksapp.databinding.FragmentRoomListBinding
import br.com.kmdev.randomlytalksapp.ui.settings.SettingsForChatSheetView
import br.com.kmdev.ui.components.setVisible
import br.com.kmdev.ui.components.toast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class RoomListFragment : Fragment() {

    private lateinit var binding: FragmentRoomListBinding
    private val viewModel: RoomListViewModel by viewModel()
    private var roomsAdapter: RoomListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRoomListBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.apply {

            roomsAdapter = RoomListAdapter().apply {
                singleActionCallback = { joinAtRoom(it.roomName) }
                longActionCallback = { showRoomOptions(it) }
            }
            rvRoomList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = roomsAdapter
            }

            fabNewChatRoom.setOnClickListener {
                NewChatSheetView().show(context = requireContext()) {
                    actionCallback = { chatName ->
                        dismissAllowingStateLoss()
                        viewModel.joinAtRoom(chatName)
                        joinAtRoom(chatName)
                    }
                }
            }
        }
    }

    private fun setupObservers() {

        viewModel.connected.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.startRoomsListener()
                viewModel.fetchRooms()
                if (viewModel.hasUserName())
                    viewModel.joinAtRoom(null)
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
        viewModel.roomListData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                hideEmptyRoomsLayout()
                roomsAdapter?.setItems(it)
            } else {
                if (viewModel.hasUserName())
                    viewModel.joinAtRoom(null)
                else
                    showEmptyRoomsLayout()
            }
        }
        viewModel.userName.observe(viewLifecycleOwner) {
            viewModel.setupConnection()
        }

        viewModel.fetchUsername()
    }

    private fun showSettingsUserName() {
        SettingsForChatSheetView().show(context = requireContext()) {
            actionCallback = { dismissAllowingStateLoss() }
        }
    }

    private fun joinAtRoom(roomName: String) {
        if (viewModel.hasUserName()) {

            // Here open chat
            findNavController().navigate(
                R.id.action_RoomList_to_Conversation, bundleOf(
                    "RoomId" to roomName
                )
            )

        } else {
            requireView().toast(getString(R.string.warning_define_your_username))
            showSettingsUserName()
        }
    }

    private fun hideEmptyRoomsLayout() {
        binding.apply {
            viewNoUserDefined.setVisible(false)
        }
    }

    private fun showEmptyRoomsLayout() {
        binding.apply {
            viewNoUserDefined.setVisible(true)
            btActionSetUsername.setOnClickListener {
                showSettingsUserName()
            }
        }
    }

    private fun showRoomOptions(roomItem: RoomItemDTO) {
        RoomOptionsSheetView().show(context = requireContext()) {
            actionCallback = { fix, hide, close ->
                when {
                    close -> dismissAllowingStateLoss()
                    fix -> {
                        roomsAdapter?.setFixedRoomStatus(roomItem, !roomItem.isFixed)
                        dismissAllowingStateLoss()
                    }
                    hide -> {
                        dismissAllowingStateLoss()
                    }
                    else -> {}
                }
            }
        }
    }

}