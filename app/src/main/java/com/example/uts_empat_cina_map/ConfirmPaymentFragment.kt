package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.bumptech.glide.Glide
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
        val backButton: Button = view.findViewById(R.id.backButton)
        val auth = FirebaseAuth.getInstance()

        // Set up spinner with dummy payment options
        val paymentMethods = arrayOf("Credit Card", "PayPal", "Cash on Delivery")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        paymentMethodSpinner.adapter = adapter

        // Calculate total price and quantity from cart items
        updateCheckoutDetails()

        confirmButton.setOnClickListener {
            // Get the current authenticated user
            val user = auth.currentUser

            val userId = user?.uid

            // Calculate total price and quantity
            val cartItems = CartManager.getCartItems()
            val totalPrice = cartItems.sumOf { it.foodItem.price * it.quantity }
            val totalQuantity = cartItems.sumOf { it.quantity }

            // Get the payment method from the spinner
            val paymentMethod = paymentMethodSpinner.selectedItem.toString()

            for(cartItem in cartItems) {
                // Create the Order object
                val order = Order(
                    userId = userId.toString(),
                    items = cartItems.map {
                        // Create an item for each cart item with necessary properties
                        OrderItem(it.foodItem.name, it.foodItem.price, it.quantity)
                    },
                    totalPrice = totalPrice,
                    totalQuantity = totalQuantity,
                    paymentMethod = paymentMethod,
                    timestamp = System.currentTimeMillis() // Current timestamp
                )


                // Save the order to Firebase Firestore
                saveOrderToFirebase(order)
            }

            // Navigate to a new activity for Google Maps (for later implementation)
            val intent = Intent(requireContext(), payment_successful::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            // Navigate to HomeFragment
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CheckoutFragment())
                .commit()
        }
    }

    private fun saveOrderToFirebase(order: Order) {
        val db = FirebaseFirestore.getInstance()
        db.collection("orders")
            .add(order)
            .addOnFailureListener { e ->
                // Handle failure
                e.printStackTrace()
            }
    }

    private fun updateCheckoutDetails() {
        val cartItems = CartManager.getCartItems()

        // Calculate total price and quantity
        var totalPrice = 0.0
        var totalQuantity = 0

        for (cartItem in cartItems) {
            totalPrice += cartItem.foodItem.price * cartItem.quantity
            totalQuantity += cartItem.quantity

        }

        // Update the UI with total price and quantity
        totalPriceTextView.text = "$${String.format("%.2f", totalPrice)}"
        totalQuantityTextView.text = "Total Quantity : $totalQuantity"
    }
}