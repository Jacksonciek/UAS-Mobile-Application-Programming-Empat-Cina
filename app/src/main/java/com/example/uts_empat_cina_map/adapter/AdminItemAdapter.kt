package com.example.uts_empat_cina_map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.R
import com.example.uts_empat_cina_map.models.FoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminItemAdapter(private var itemList: List<FoodItem>) :
    RecyclerView.Adapter<AdminItemAdapter.AdminItemViewHolder>() {

    private var filteredList = itemList.toMutableList()
    private val firestore = FirebaseFirestore.getInstance()

    class AdminItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.itemName)
        val stockEditText: EditText = view.findViewById(R.id.stockEditText)
        val updateButton: Button = view.findViewById(R.id.updateButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin, parent, false)
        return AdminItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminItemViewHolder, position: Int) {
        val item = filteredList[position]
        holder.itemName.text = item.name
        holder.stockEditText.setText(item.stock.toString())

        // Update stock on button click
        holder.updateButton.setOnClickListener {
            val newStock = holder.stockEditText.text.toString().toIntOrNull()
            if (newStock != null) {
                // Update stock in the local list
                item.stock = newStock
                notifyItemChanged(position)

                // Update the stock in Firestore
                updateStockInFirestore(item.name, newStock)
            }
        }
    }

    override fun getItemCount(): Int = filteredList.size

    // Filter items based on search query
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            itemList.toMutableList()
        } else {
            itemList.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }
        notifyDataSetChanged()
    }

    // Update the stock in Firestore
    private fun updateStockInFirestore(itemName: String, newStock: Int) {
        // Query to find the item by name
        val query = firestore.collection("items").whereEqualTo("name", itemName)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Assuming that the name is unique, get the first document
                    val itemDocument = querySnapshot.documents[0]

                    // Update the stock for this item
                    itemDocument.reference.update("stock", newStock)
                        .addOnSuccessListener {
                            // Stock updated successfully
                        }
                        .addOnFailureListener { exception ->
                            // Handle failure
                            exception.printStackTrace()
                        }
                } else {
                    // Handle case where no item with the specified name is found
                    println("No item found with the name: $itemName")
                }
            }
            .addOnFailureListener { exception ->
                // Handle query failure
                exception.printStackTrace()
            }
    }
}
