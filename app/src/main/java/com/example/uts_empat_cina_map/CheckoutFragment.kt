package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Spinner
import com.example.uts_empat_cina_map.OrderData.CartManager

class CheckoutFragment : Fragment() {

    //fetch payment method firebase

    private lateinit var paymentMethodSpinner: Spinner
    private lateinit var confirmButton: Button
    private lateinit var totalPriceTextView: TextView
    private lateinit var totalQuantityTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentMethodSpinner = view.findViewById(R.id.paymentMethodSpinner)
        confirmButton = view.findViewById(R.id.confirmButton)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        totalQuantityTextView = view.findViewById(R.id.totalQuantityTextView)

        // Set up spinner with dummy payment options
        val paymentMethods = arrayOf("Credit Card", "PayPal", "Cash on Delivery")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        paymentMethodSpinner.adapter = adapter

        // Calculate total price and quantity from cart items
        updateCheckoutDetails()

        confirmButton.setOnClickListener {
            // Navigate to a new activity for Google Maps (for later implementation)
            val intent = Intent(requireContext(), payment_successful::class.java)
            startActivity(intent)
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

        // Update the UI
        totalPriceTextView.text = "Total Price: $${String.format("%.2f", totalPrice)}"
        totalQuantityTextView.text = "Total Quantity: $totalQuantity"
    }
}
