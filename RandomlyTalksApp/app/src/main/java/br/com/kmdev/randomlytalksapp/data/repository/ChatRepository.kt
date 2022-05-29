package br.com.kmdev.randomlytalksapp.data.repository

import androidx.lifecycle.MutableLiveData
import br.com.kmdev.randomlytalksapp.data.models.RequestResult
import br.com.kmdev.randomlytalksapp.data.models.SendMessage
import br.com.kmdev.randomlytalksapp.data.models.SubscribeRoom
import br.com.kmdev.randomlytalksapp.data.viewdata.ChatMessageItemDTO
import br.com.kmdev.randomlytalksapp.data.viewdata.RoomItemDTO
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun setupConnection(): Flow<RequestResult<Boolean>>

    suspend fun setupRoomsListener()

    suspend fun roomList(roomListData: MutableLiveData<MutableList<RoomItemDTO>>)

    suspend fun joinedAtRoomListener(usersJoinedData: MutableLiveData<String>)

    suspend fun joinAtRoom(data: SubscribeRoom): Flow<RequestResult<Boolean>>

    suspend fun messagesListener(
        userName: String?,
        roomId: String,
        messages: MutableLiveData<ChatMessageItemDTO>
    )

    suspend fun sendMessage(sendMessage: SendMessage)

}