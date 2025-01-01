package com.example.uts_empat_cina_map.OrderData

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.R
import com.example.uts_empat_cina_map.model.Order

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val orderIdTextView: TextView = itemView.findViewById(R.id.orderId)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantity)
        private val totalAmountTextView: TextView = itemView.findViewById(R.id.totalAmount)
        private val statusTextView: TextView = itemView.findViewById(R.id.textView7)  // Status TextView
        private val itemsTextView: TextView = itemView.findViewById(R.id.itemsTextView) // New TextView

        fun bind(order: Order) {
            orderIdTextView.text = "Order ID: ${order.id}"
            quantityTextView.text = "Quantity: ${order.totalQuantity}"
            totalAmountTextView.text = "Total Amount: Rp. ${order.totalPrice}"

            // Set the order status in textView7
            statusTextView.text =
                order.orderStatus  // "Accepted", "On Process", or any other status
            if (order.orderStatus == "Accepted") {
                statusTextView.setTextColor(itemView.context.getColor(R.color.green)) // Change color to green
            }else if(order.orderStatus == "Declined") {
                statusTextView.setTextColor(itemView.context.getColor(R.color.red)) // Change color to red
            }else {
                statusTextView.setTextColor(itemView.context.getColor(R.color.gray)) // Default color or orange
            }

            // Create a string with item names and their respective quantities
            val itemNamesAndQuantities = StringBuilder()
            for (item in order.items) {
                itemNamesAndQuantities.append("${item.name}: ${item.quantity}\n")
            }

            // Set the item names and quantities in the new TextView
            itemsTextView.text = itemNamesAndQuantities.toString()
        }
    }
}
