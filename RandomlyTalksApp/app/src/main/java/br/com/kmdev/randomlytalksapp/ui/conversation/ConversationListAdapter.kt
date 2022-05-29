package br.com.kmdev.randomlytalksapp.ui.conversation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.kmdev.randomlytalksapp.data.viewdata.ChatMessageItemDTO
import br.com.kmdev.randomlytalksapp.databinding.ItemViewConversationBinding
import br.com.kmdev.ui.components.setVisible

class ConversationListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adapterData: ArrayList<ChatMessageItemDTO> = arrayListOf()
    var longActionCallback: ((item: ChatMessageItemDTO) -> Unit)? = null
    var singleActionCallback: ((item: ChatMessageItemDTO) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MessageItemView(
            ItemViewConversationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MessageItemView)
            holder.bind(adapterData[position])
    }

    override fun getItemCount() = adapterData.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(listItems: List<ChatMessageItemDTO>) {
        adapterData.addAll(listItems)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(item: ChatMessageItemDTO) {
        adapterData.add(item)
        notifyItemInserted(adapterData.size - 1)
    }

    inner class MessageItemView(private val itemViewBinding: ItemViewConversationBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(data: ChatMessageItemDTO) = with(itemView) {
            itemViewBinding.apply {

                viewChatFromUser.setVisible(!data.messageFromMe)
                viewChatFromMe.setVisible(data.messageFromMe)

                if (data.messageFromMe)
                    tvMessageToUser.text = data.messageContent
                else {
                    tvMessageFromUserName.text = data.userName
                    tvMessageFromUser.text = data.messageContent
                }

                itemView.setOnClickListener {
                    singleActionCallback?.invoke(data)
                }
                itemView.setOnLongClickListener {
                    longActionCallback?.invoke(data)
                    return@setOnLongClickListener true
                }
            }
        }
    }

}