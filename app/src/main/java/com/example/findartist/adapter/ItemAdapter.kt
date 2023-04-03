package com.example.findartist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.findartist.R
import com.example.findartist.model.ArtistItemList

class ItemAdapter(private val context: Context,
                  private val dataset: List<ArtistItemList>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val profilePhoto: ImageView = view.findViewById(R.id.list_item_profile_photo)
        val artistName: TextView = view.findViewById(R.id.list_item_name)
        val artistJob: TextView = view.findViewById(R.id.list_item_job)
        val artistRate: TextView = view.findViewById(R.id.list_item_rate)
        val artistDescription: TextView = view.findViewById(R.id.list_item_description)
    }


    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false);

        return ItemViewHolder(adapterLayout);
    }

    override fun getItemCount() = dataset.size

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        val rating = "Rate: " + context.resources.getString(item.rateResourceId) + "/5"
        holder.profilePhoto.setImageResource(item.profilePhotoResourceId)
        holder.artistName.text = context.resources.getString(item.nameResourceId)
        holder.artistJob.text = context.resources.getString(item.jobResourceId)
        holder.artistRate.text = rating
        holder.artistDescription.text = context.resources.getString(item.descriptionResourceId)
    }

}