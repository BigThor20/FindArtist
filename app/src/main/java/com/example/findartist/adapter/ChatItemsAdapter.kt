package com.example.findartist.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findartist.R
import com.example.findartist.model.ChatItemList

class ChatItemsAdapter(private val chatItems: List<ChatItemList>) : RecyclerView.Adapter<ChatItemsAdapter.ChatItemViewHolder>() {

    private var onChatItemClickListener: OnChatItemClickListener? = null

    interface OnChatItemClickListener {
        fun onChatItemClick(chatItem: ChatItemList)
    }

    fun setOnChatItemClickListener(listener: OnChatItemClickListener) {
        onChatItemClickListener = listener
    }

    class ChatItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePhotoImageView: ImageView = itemView.findViewById(R.id.chatItem_profile_photo)
        val nameTextView: TextView = itemView.findViewById(R.id.chatItem_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        val chatItem = chatItems[position]

        Glide.with(holder.itemView)
            .load(chatItem.profilePhotoUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground) // Adaugă o imagine placeholder
            .into(holder.profilePhotoImageView)
        holder.nameTextView.text = chatItem.name

        holder.itemView.setOnClickListener {
            onChatItemClickListener?.onChatItemClick(chatItem)
        }
    }

    override fun getItemCount(): Int {
        return chatItems.size
    }
}
