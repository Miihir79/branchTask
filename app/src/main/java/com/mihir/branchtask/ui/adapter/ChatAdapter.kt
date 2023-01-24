package com.mihir.branchtask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mihir.branchtask.databinding.ItemChatBinding
import com.mihir.branchtask.model.MessageItem
import com.mihir.branchtask.ui.formatDate

class ChatAdapter: RecyclerView.Adapter<ChatAdapter.ViewHolder>()   {

    private var chat = ArrayList<MessageItem>()

    inner class ViewHolder(private val binding: ItemChatBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageItem) = binding.apply {
            textViewHeading.text = item.user_id
            textViewDesc.text = item.body
            textViewDate.text = item.timestamp.formatDate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ItemChatBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount() = chat.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chat[position])
    }

    fun setData(data:ArrayList<MessageItem>){
        chat = data.reversed() as ArrayList<MessageItem>
    }

    fun addOurMessage(mess:MessageItem){
        chat.add(mess)
        notifyItemInserted(chat.size - 1)
    }

    fun setSingleData(mess:MessageItem){
        chat.add(mess)
    }
}