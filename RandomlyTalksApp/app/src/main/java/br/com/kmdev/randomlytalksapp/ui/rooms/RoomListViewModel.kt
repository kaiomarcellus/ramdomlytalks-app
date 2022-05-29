package br.com.kmdev.randomlytalksapp.ui.rooms

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.kmdev.randomlytalksapp.data.models.ChatConstants
import br.com.kmdev.randomlytalksapp.data.models.SubscribeRoom
import br.com.kmdev.randomlytalksapp.data.repository.ChatRepository
import br.com.kmdev.randomlytalksapp.data.repository.SettingsRepository
import br.com.kmdev.randomlytalksapp.data.viewdata.RoomItemDTO
import kotlinx.coroutines.launch
import timber.log.Timber

class RoomListViewModel(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val userName = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()
    val connected = MutableLiveData<Boolean>()
    val roomListData = MutableLiveData<MutableList<RoomItemDTO>>()
    val usersJoinedData = MutableLiveData<String>()

    fun fetchUsername() {
        viewModelScope.launch {
            settingsRepository.fetchUsername().collect {
                userName.value = it
            }
        }
    }

    fun setupConnection() {
        viewModelScope.launch {
            chatRepository.setupConnection().collect {
                if (it.isSuccess())
                    connected.postValue(true)
                else if (it.isError()) {
                    connected.postValue(false)
                    errorMessage.postValue(it.errorMessage ?: "")
                }
            }
        }
    }

    fun startRoomsListener() {
        viewModelScope.launch { chatRepository.setupRoomsListener() }
    }

    fun fetchRooms() {
        viewModelScope.launch { chatRepository.roomList(roomListData) }
    }

    fun joinAtRoom(room: String? = null) {
        Timber.e("Joined at room > $room")
        viewModelScope.launch {
            chatRepository.joinAtRoom(
                SubscribeRoom(
                    roomId = room ?: ChatConstants.DEFAULT_ROOM,
                    userName = userName.value
                )
            ).collect {}
        }
    }

    fun hasUserName() = userName.value?.isNotEmpty() ?: false

}