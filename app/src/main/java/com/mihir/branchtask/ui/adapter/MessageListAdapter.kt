package com.mihir.branchtask.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mihir.branchtask.databinding.ItemThreadChatBinding
import com.mihir.branchtask.model.MessageItem
import com.mihir.branchtask.ui.formatDate
import kotlin.collections.ArrayList

class MessageListAdapter(
    private var onItemClicked: ((clickPosition: Int) -> Unit)
): RecyclerView.Adapter<MessageListAdapter.ViewHolder>()  {

    private var chats = ArrayList<MessageItem>() //TODO: make a new data class for thread wise sorted items

    inner class ViewHolder(private val binding: ItemThreadChatBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:MessageItem) = binding.apply {

            textViewHeading.text = item.thread_id.toString()
            textViewDesc.text = item.body
            textViewDate.text = item.timestamp.formatDate()

            root.setOnClickListener {
                onItemClicked(item.thread_id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(ItemThreadChatBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemCount() = chats.size

    fun setChatItems(chatItems:ArrayList<MessageItem>){
        chats = chatItems
    }
}