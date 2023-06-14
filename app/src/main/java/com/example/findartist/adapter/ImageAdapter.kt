package com.example.findartist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findartist.R

class ImageAdapter(private val images: ArrayList<String>?) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.portfolio_image)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_portfolio_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        val imageResId = images[position]
//        holder.imageView.setImageResource(imageResId)
        val imageUrl = images?.get(position)
        Glide.with(holder.itemView)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground) // Adaugă o imagine placeholder
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images?.size ?: 0
    }
}
