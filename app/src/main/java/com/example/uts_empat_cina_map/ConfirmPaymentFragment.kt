package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.uts_empat_cina_map.OrderData.CartManager
import com.example.uts_empat_cina_map.model.Order
import com.example.uts_empat_cina_map.model.OrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ConfirmPaymentFragment : Fragment() {
    private lateinit var paymentMethodSpinner: Spinner
    private lateinit var confirmButton: Button
    private lateinit var totalPriceTextView: TextView
    private lateinit var totalQuantityTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarContainer: FrameLayout
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentMethodSpinner = view.findViewById(R.id.paymentMethodSpinner)
        confirmButton = view.findViewById(R.id.confirmButton)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        totalQuantityTextView = view.findViewById(R.id.totalQuantityTextView)
        progressBar = view.findViewById(R.id.progressBar)
        progressBarContainer = view.findViewById(R.id.progressBarContainer)
        val backButton: Button = view.findViewById(R.id.backButton)

        // Fetch payment methods from Firebase and set up the spinner
        fetchPaymentMethods()

        // Calculate total price and quantity from cart items
        updateCheckoutDetails()

        confirmButton.setOnClickListener {
            progressBarContainer.visibility = View.VISIBLE
            confirmButton.isEnabled = false

            val user = auth.currentUser
            val userId = user?.uid

            val cartItems = CartManager.getCartItems()
            val totalPrice = cartItems.sumOf { it.foodItem.price * it.quantity }
            val totalQuantity = cartItems.sumOf { it.quantity }

            val paymentMethod = paymentMethodSpinner.selectedItem.toString()

            for (cartItem in cartItems) {
                val order = Order(
                    userId = userId.toString(),
                    items = cartItems.map {
                        OrderItem(it.foodItem.name, it.foodItem.price, it.quantity)
                    },
                    totalPrice = totalPrice,
                    totalQuantity = totalQuantity,
                    paymentMethod = paymentMethod,
                    timestamp = System.currentTimeMillis()
                )

                saveOrderToFirebase(order)
            }
        }

        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CheckoutFragment())
                .commit()
        }
    }

    private fun fetchPaymentMethods() {
        val user = auth.currentUser
        val userId = user?.uid ?: return

        // Show loading spinner
        progressBarContainer.visibility = View.VISIBLE

        firestore.collection("user_payments")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val paymentMethods = querySnapshot.documents.map { document ->
                    document.getString("paymentType") ?: "Unknown"
                }

                // Update spinner with fetched payment methods
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    paymentMethods
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                paymentMethodSpinner.adapter = adapter

                // Hide loading spinner
                progressBarContainer.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to fetch payment methods", Toast.LENGTH_SHORT).show()
                progressBarContainer.visibility = View.GONE
            }
    }

    private fun saveOrderToFirebase(order: Order) {
        firestore.collection("orders")
            .add(order)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                confirmButton.isEnabled = true
                val intent = Intent(requireContext(), payment_successful::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                confirmButton.isEnabled = true
                e.printStackTrace()
            }
    }

    private fun updateCheckoutDetails() {
        val cartItems = CartManager.getCartItems()

        var totalPrice = 0.0
        var totalQuantity = 0

        for (cartItem in cartItems) {
            totalPrice += cartItem.foodItem.price * cartItem.quantity
            totalQuantity += cartItem.quantity
        }

        totalPriceTextView.text = "$${String.format("%.2f", totalPrice)}"
        totalQuantityTextView.text = "Total Quantity : $totalQuantity"
    }
}
