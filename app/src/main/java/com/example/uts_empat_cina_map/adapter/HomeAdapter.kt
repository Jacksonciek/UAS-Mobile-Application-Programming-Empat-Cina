package com.example.uts_empat_cina_map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.HomeItem
import com.example.uts_empat_cina_map.R
import com.squareup.picasso.Picasso // Assuming you use Picasso for image loading

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var items: List<HomeItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = items[position]
        println("Binding item at position $position: $item")  // Log item details

        holder.nameTextView.text = item.name
        holder.daysLeftTextView.text = "${item.daysLeft} days left"

        // Log image URL and binding process
        val imageUrl = item.photo
        println("Loading image for: $imageUrl")

        if (imageUrl.isNullOrEmpty()) {
            // Load a placeholder image if the URL is empty or invalid
            Picasso.get().load(R.drawable.placeholder_image)  // Replace with your placeholder image
                .into(holder.photoImageView)
        } else {
            Picasso.get().load(imageUrl).into(holder.photoImageView)
        }
    }



    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(newItems: List<HomeItem>) {
        println("Updating items list. New size: ${newItems.size}")  // Log number of new items
        items = newItems
        notifyDataSetChanged()
    }


    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val daysLeftTextView: TextView = itemView.findViewById(R.id.daysLeftTextView)
    }
}
