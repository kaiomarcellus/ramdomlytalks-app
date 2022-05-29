package br.com.kmdev.randomlytalksapp.data.models

data class SendMessage(
    var dateMillis: Long = System.currentTimeMillis(),
    var roomId: String? = null,
    var userName: String? = null,
    var messageContent: String
)