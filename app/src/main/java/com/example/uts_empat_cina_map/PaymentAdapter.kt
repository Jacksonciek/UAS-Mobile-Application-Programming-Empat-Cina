package com.example.uts_empat_cina_map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.OrderData.PaymentMethod

class PaymentAdapter(private val paymentList: List<PaymentMethod>) :
    RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = paymentList[position]
        holder.bind(payment)
    }

    override fun getItemCount(): Int = paymentList.size

    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val paymentTypeTextView: TextView = itemView.findViewById(R.id.paymentTypeTextView)
        private val labelNameTextView: TextView = itemView.findViewById(R.id.labelNameTextView)
        private val cardNumberTextView: TextView = itemView.findViewById(R.id.cardNumberTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(payment: PaymentMethod) {
            paymentTypeTextView.text = payment.paymentType
            labelNameTextView.text = payment.labelName
            cardNumberTextView.text = payment.cardNumber
            descriptionTextView.text = payment.description
        }
    }
}
