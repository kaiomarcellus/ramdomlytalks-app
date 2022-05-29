package br.com.kmdev.randomlytalksapp.data.viewdata

import com.google.gson.annotations.SerializedName

data class ChatMessageItemDTO(
    @SerializedName("userName")
    val userName: String,
    @SerializedName("messageContent")
    val messageContent: String,
    @SerializedName("roomId")
    val roomId: String,
    @SerializedName("dateMillis")
    val dateMillis: Long,
    var messageFromMe: Boolean = false
)