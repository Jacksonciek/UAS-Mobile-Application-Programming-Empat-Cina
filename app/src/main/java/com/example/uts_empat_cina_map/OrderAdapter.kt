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
//        private val dateTextView: TextView = itemView.findViewById(R.id.date)
        private val quantityTextView: TextView = itemView.findViewById(R.id.quantity)
        private val totalAmountTextView: TextView = itemView.findViewById(R.id.totalAmount)
//        private val detailTextView: TextView = itemView.findViewById(R.id.detailText)
//        private val deliveredTextView: TextView = itemView.findViewById(R.id.deliveredText)

        fun bind(order: Order) {
            orderIdTextView.text = "Order ID: ${order.id}"
            quantityTextView.text = "Quantity: ${order.totalQuantity}"
            totalAmountTextView.text = "Total Amount: Rp. ${order.totalPrice}"

            // If the order status is 'Delivered', show 'Delivered' text in green
//            if (order.status == "Delivered") {
//                deliveredTextView.text = "Delivered"
//                deliveredTextView.setTextColor(Color.parseColor("#27AE60"))
//            } else {
//                deliveredTextView.text = "Pending"
//                deliveredTextView.setTextColor(Color.parseColor("#E74C3C"))
//            }

            // Set up detail button to navigate to order details (if needed)
//            detailTextView.setOnClickListener {
                // Navigate to Order Details Fragment or Activity
//            }
        }
    }
}