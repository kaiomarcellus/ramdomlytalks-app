package br.com.kmdev.randomlytalksapp.ui.conversation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.kmdev.randomlytalksapp.data.models.SendMessage
import br.com.kmdev.randomlytalksapp.data.models.SubscribeRoom
import br.com.kmdev.randomlytalksapp.data.repository.ChatRepository
import br.com.kmdev.randomlytalksapp.data.repository.SettingsRepository
import br.com.kmdev.randomlytalksapp.data.viewdata.ChatMessageItemDTO
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val userName = MutableLiveData<String>()
    val errorMessage = MutableLiveData<String>()
    val connected = MutableLiveData<Boolean>()
    val roomMessagesData = MutableLiveData<ChatMessageItemDTO>()

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

    fun joinAtRoom(room: String) {
        viewModelScope.launch {
            chatRepository.joinAtRoom(
                SubscribeRoom(
                    roomId = room,
                    userName = userName.value
                )
            ).collect {

            }
        }
    }

    fun fetchMessages(roomId: String) {
        viewModelScope.launch {
            chatRepository.messagesListener(userName.value, roomId, roomMessagesData)
        }
    }

    fun sendMessage(roomId: String, contentText: String) {
        viewModelScope.launch {
            chatRepository.sendMessage(
                SendMessage(
                    userName = userName.value,
                    roomId = roomId,
                    messageContent = contentText
                )
            )
        }
    }

}