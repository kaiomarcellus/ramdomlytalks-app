package br.com.kmdev.randomlytalksapp.data.repository

import androidx.lifecycle.MutableLiveData
import br.com.kmdev.randomlytalksapp.data.models.ChatConstants
import br.com.kmdev.randomlytalksapp.data.models.RequestResult
import br.com.kmdev.randomlytalksapp.data.models.SendMessage
import br.com.kmdev.randomlytalksapp.data.models.SubscribeRoom
import br.com.kmdev.randomlytalksapp.data.viewdata.ChatMessageItemDTO
import br.com.kmdev.randomlytalksapp.data.viewdata.RoomItemDTO
import com.google.gson.Gson
import io.socket.client.Socket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONArray

class ChatRemoteRepository(
    private val socketConnection: Socket,
    private val gson: Gson
) : ChatRepository {

    override suspend fun setupConnection(): Flow<RequestResult<Boolean>> {
        return flow {
            emit(RequestResult.loading())
            try {
                socketConnection.connect()
                emit(RequestResult.success(socketConnection.connected()))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(RequestResult.error(e.localizedMessage))
            }
        }
    }

    override suspend fun setupRoomsListener() {
        try {
            socketConnection.emit(ChatConstants.EVENT_ROOM_LIST)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun roomList(roomListData: MutableLiveData<MutableList<RoomItemDTO>>) {
        try {
            socketConnection.on(ChatConstants.EVENT_ROOM_LIST) { roomList ->

                val rooms = mutableListOf<RoomItemDTO>()
                roomList.map { it as JSONArray }.map {
                    for (i in 0 until it.length())
                        rooms.add(
                            RoomItemDTO(
                                id = i, roomName = it[i] as String, isFixed = false
                            )
                        )
                }
                roomListData.postValue(rooms)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun joinedAtRoomListener(usersJoinedData: MutableLiveData<String>) {
        try {
            socketConnection.on(ChatConstants.EVENT_USER_ENTERED_ROOM) { data ->
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun joinAtRoom(data: SubscribeRoom): Flow<RequestResult<Boolean>> {
        return flow {
            emit(RequestResult.loading())
            try {
                socketConnection.emit(ChatConstants.EVENT_ROOM_SUBSCRIBE, gson.toJson(data))
                emit(RequestResult.success(true))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(RequestResult.error(e.localizedMessage))
            }
        }
    }

    override suspend fun messagesListener(
        userName: String?,
        roomId: String,
        messages: MutableLiveData<ChatMessageItemDTO>
    ) {
        try {
            socketConnection.on(ChatConstants.EVENT_CHAT_UPDATED) { data ->
                val newMessage = gson.fromJson(
                    data[0].toString(),
                    ChatMessageItemDTO::class.java
                ).apply {
                    messageFromMe = userName == this.userName
                }

                if (newMessage.roomId == roomId)
                    messages.postValue(newMessage)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun sendMessage(sendMessage: SendMessage) {
        try {
            socketConnection.emit(ChatConstants.EVENT_SEND_MESSAGE, gson.toJson(sendMessage))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}