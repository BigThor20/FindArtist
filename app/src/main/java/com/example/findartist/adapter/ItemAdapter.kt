package com.example.findartist.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findartist.R
import com.example.findartist.model.ArtistItemList

class ItemAdapter(private val context: Context,
                  private val dataset: List<ArtistItemList>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private lateinit var mListener: OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(position: Int, id:String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }



    inner class ItemViewHolder(private val view: View,
                               private val listener: OnItemClickListener
                         ): RecyclerView.ViewHolder(view) {
        val profilePhoto: ImageView = view.findViewById(R.id.list_item_profile_photo)
        val artistName: TextView = view.findViewById(R.id.list_item_name)
        val artistJob: TextView = view.findViewById(R.id.list_item_job)
        val artistRate: TextView = view.findViewById(R.id.list_item_rate)
        val artistDescription: TextView = view.findViewById(R.id.list_item_description)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, dataset[position].id)
            }
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false);

        return ItemViewHolder(adapterLayout, mListener);
    }

    override fun getItemCount() = dataset.size

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        val rating = "Rate: ${item.rate}/5"
        val name = item.firstName + " " + item.lastName
        // Utilizați Glide pentru a încărca imaginea de profil de la URL
        Glide.with(context)
            .load(item.profilePhotoUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground) // Puneți o imagine placeholder
            .into(holder.profilePhoto)
        holder.artistName.text = name
        holder.artistJob.text = item.job
        holder.artistRate.text = rating
        holder.artistDescription.text = item.description
    }


}