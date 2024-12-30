package com.example.uts_empat_cina_map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.R
import com.example.uts_empat_cina_map.model.Order

class OrdersAdapter(
    private val orders: List<Order>, // List of orders to display
    private val onAcceptOrder: (Order) -> Unit // Lambda for handling button clicks
) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    // ViewHolder class to hold references to the views for each order item
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemsTextView: TextView = itemView.findViewById(R.id.itemsTextView)
        val orderIdTextView: TextView = itemView.findViewById(R.id.orderIdTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.quantityAdmin)
        val totalPriceTextView: TextView = itemView.findViewById(R.id.totalPriceTextView)
        val acceptButton: Button = itemView.findViewById(R.id.acceptButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_admin, parent, false) // Inflate the item layout
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.orderIdTextView.text = "Order ID: ${order.id}"
        holder.totalPriceTextView.text = "Total: Rp.${order.totalPrice}"
        holder.quantityTextView.text = "Quantity: ${order.totalQuantity}"
        // Prepare a string to display all item names and quantities
        val itemsList = StringBuilder()
        for (item in order.items) {
            itemsList.append("${item.name}: ${item.quantity}\n")
        }

        // Set the items and quantities to the TextView
        holder.itemsTextView.text = itemsList.toString()

        // Set click listener for the accept button
        holder.acceptButton.setOnClickListener { onAcceptOrder(order) }
    }


    override fun getItemCount(): Int {
        return orders.size // Return the number of items
    }

}