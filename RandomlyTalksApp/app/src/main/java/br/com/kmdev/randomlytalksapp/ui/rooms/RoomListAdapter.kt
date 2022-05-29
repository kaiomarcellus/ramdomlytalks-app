package br.com.kmdev.randomlytalksapp.ui.rooms

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.kmdev.randomlytalksapp.data.viewdata.RoomItemDTO
import br.com.kmdev.randomlytalksapp.databinding.ItemViewRoomBinding
import br.com.kmdev.ui.components.setVisible

class RoomListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adapterData: ArrayList<RoomItemDTO> = arrayListOf()
    var longActionCallback: ((item: RoomItemDTO) -> Unit)? = null
    var singleActionCallback: ((item: RoomItemDTO) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoomItemView(
            ItemViewRoomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RoomItemView)
            holder.bind(adapterData[position])
    }

    override fun getItemCount() = adapterData.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(listItems: List<RoomItemDTO>) {
        adapterData.clear()
        adapterData.addAll(listItems.sortedByDescending { it.isFixed })
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFixedRoomStatus(roomItemDTO: RoomItemDTO, fixed: Boolean) {
        adapterData.firstOrNull { it.roomName == roomItemDTO.roomName }?.isFixed = fixed

        val tempData = adapterData.sortedByDescending { it.isFixed }
        adapterData.clear()
        adapterData.addAll(tempData)
        notifyDataSetChanged()
    }

    inner class RoomItemView(private val itemViewBinding: ItemViewRoomBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(data: RoomItemDTO) = with(itemView) {
            itemViewBinding.apply {

                ivRoomFixedIndicator.setVisible(data.isFixed)
                tvRoomTitle.text = data.roomName
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