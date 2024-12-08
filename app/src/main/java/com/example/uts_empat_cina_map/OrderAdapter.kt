package com.example.uts_empat_cina_map.OrderData

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uts_empat_cina_map.R

class OrderAdapter(
    private val cartItems: List<CartItem>,
    private val onDeleteClick: (CartItem) -> Unit
) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImageView: ImageView = itemView.findViewById(R.id.foodImageView)
        val foodNameTextView: TextView = itemView.findViewById(R.id.foodNameTextView)
        val foodPriceTextView: TextView = itemView.findViewById(R.id.foodPriceTextView)
        val foodQuantityTextView: TextView = itemView.findViewById(R.id.foodQuantityTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItems[position]

        Glide.with(holder.itemView.context)
            .load(cartItem.foodItem.imageUrl)
            .into(holder.foodImageView)

        holder.foodNameTextView.text = cartItem.foodItem.name
        holder.foodPriceTextView.text = "Price: $${cartItem.foodItem.price}"
        holder.foodQuantityTextView.text = "Quantity: ${cartItem.quantity}"

        holder.deleteButton.setOnClickListener {
            onDeleteClick(cartItem)
        }
    }

    override fun getItemCount(): Int = cartItems.size
}
