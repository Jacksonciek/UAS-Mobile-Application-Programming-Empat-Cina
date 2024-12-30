package com.example.uts_empat_cina_map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.adapter.OrdersAdapter
import com.example.uts_empat_cina_map.model.Order
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class AdminStatsFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var totalProfitTextView: TextView
    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var ordersAdapter: OrdersAdapter
    private val ordersList = mutableListOf<Order>()
    private val adminViewModel: AdminViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_stats, container, false)

        totalProfitTextView = view.findViewById(R.id.totalProfitTextView)
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView)
        firestore = FirebaseFirestore.getInstance()

        setupRecyclerView()
        fetchOrders()

        return view
    }

    private fun setupRecyclerView() {
        ordersAdapter = OrdersAdapter(ordersList) { order -> acceptOrder(order) }
        ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        ordersRecyclerView.adapter = ordersAdapter
    }

    private fun fetchOrders() {
        firestore.collection("orders")
            .whereEqualTo("orderStatus", "On Process")
            .get()
            .addOnSuccessListener { querySnapshot ->
                ordersList.clear()
                for (document in querySnapshot) {
                    val order = document.toObject(Order::class.java)
                    ordersList.add(order)
                }
                ordersAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("AdminStatsFragment", "Error fetching orders", exception)
            }

        fetchTotalProfit()
    }

    private fun fetchTotalProfit() {
        firestore.collection("orders")
            .whereEqualTo("orderStatus", "Accepted")
            .get()
            .addOnSuccessListener { querySnapshot ->
                var totalProfit = 0.0
                for (document in querySnapshot) {
                    val totalPrice = document.getDouble("totalPrice") ?: 0.0
                    totalProfit += totalPrice
                }
                totalProfitTextView.text = "Total Profit: Rp. $totalProfit"
                adminViewModel.setTotalProfit(totalProfit)
            }
            .addOnFailureListener { exception ->
                Log.e("AdminStatsFragment", "Error fetching profit", exception)
            }
    }

    private fun acceptOrder(order: Order) {
        val db = FirebaseFirestore.getInstance()

        db.runTransaction { transaction ->
            // Step 1: Fetch the order document
            val orderRef = db.collection("orders").document(order.id!!)
            val snapshot = transaction.get(orderRef)
            val currentStatus = snapshot.getString("orderStatus") ?: "On Process"

            // Check if the order is already accepted
            if (currentStatus != "On Process") {
                throw FirebaseFirestoreException(
                    "Order is not in 'On Process' status",
                    FirebaseFirestoreException.Code.ABORTED
                )
            }

            // Step 2: Fetch stock for all items in the order
            val stockUpdates = mutableMapOf<String, Long>() // Map to track stock updates
            for (item in order.items) {
                // Fetch item using a query, but you need to handle this outside the transaction
                val query = db.collection("items")
                    .whereEqualTo("name", item.name)
                    .limit(1) // Limit to a single result

                // Get the first document from the query result (done outside the transaction)
                val querySnapshot = Tasks.await(query.get()) // Use await() to ensure task completes

                // Check if the query returned any documents
                if (querySnapshot.isEmpty) {
                    throw FirebaseFirestoreException(
                        "Item ${item.name} not found in the database",
                        FirebaseFirestoreException.Code.ABORTED
                    )
                }

                // Get the first document (assuming there is only one due to the limit)
                val itemDoc = querySnapshot.documents.first()

                // Get the 'stock' field from the document
                val currentStock = itemDoc.getLong("stock") ?: 0L

                // Log for debugging
                Log.d("AcceptOrder", "Current stock for ${item.name}: $currentStock")

                // Check stock availability
                if (currentStock < item.quantity) {
                    throw FirebaseFirestoreException(
                        "Insufficient stock for ${item.name}",
                        FirebaseFirestoreException.Code.ABORTED
                    )
                }

                // Store the updated stock in the map
                stockUpdates[itemDoc.id] = currentStock - item.quantity
            }

            // Step 3: Perform writes (after all reads)
            // Update the order status to "Accepted"
            transaction.update(orderRef, "orderStatus", "Accepted")

            // Update stock values for each item
            for ((itemDocId, updatedStock) in stockUpdates) {
                val itemRef = db.collection("items").document(itemDocId)
                transaction.update(itemRef, "stock", updatedStock)
            }
        }.addOnSuccessListener {
            // Successfully updated the order and stock
            Toast.makeText(context, "Order accepted successfully!", Toast.LENGTH_SHORT).show()

            fetchOrders()
        }.addOnFailureListener { e ->
            // Handle errors
            Log.e("OrdersAdapter", "Error accepting order", e)
            Toast.makeText(context, "Error accepting order: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
