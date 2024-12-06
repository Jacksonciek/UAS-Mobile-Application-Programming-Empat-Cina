package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Spinner
import com.example.uts_empat_cina_map.OrderData.CartManager

// Updated CheckoutFragment.kt
class CheckoutFragment : Fragment() {

    //fetch payment method firebase

    private lateinit var paymentMethodSpinner: Spinner
    private lateinit var confirmButton: Button
    private lateinit var totalPriceTextView: TextView
    private lateinit var totalQuantityTextView: TextView
    private lateinit var itemListLayout: LinearLayout

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
        itemListLayout = view.findViewById(R.id.itemListLayout)
        val backButton: Button = view.findViewById(R.id.backButton) // Reference to the backButton

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

        backButton.setOnClickListener {
            // Navigate to HomeFragment
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()) // Replace with your container ID
                .addToBackStack(null) // Optional, to add to the back stack
                .commit()
        }
    }


    private fun updateCheckoutDetails() {
        val cartItems = CartManager.getCartItems()

        // Calculate total price and quantity
        var totalPrice = 0.0
        var totalQuantity = 0

        itemListLayout.removeAllViews()

        for (cartItem in cartItems) {
            totalPrice += cartItem.foodItem.price * cartItem.quantity
            totalQuantity += cartItem.quantity

            val itemLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val itemNameTextView = TextView(requireContext()).apply {
                text = cartItem.foodItem.name
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                textSize = 18f
                setTextColor(resources.getColor(R.color.black, null))
            }

            val itemQuantityTextView = TextView(requireContext()).apply {
                text = cartItem.quantity.toString()
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                textSize = 18f
                setTextColor(resources.getColor(R.color.black, null))
            }

            itemLayout.addView(itemNameTextView)
            itemLayout.addView(itemQuantityTextView)
            itemListLayout.addView(itemLayout)
        }

        // Update the UI
        totalPriceTextView.text = "$${String.format("%.2f", totalPrice)}"
        totalQuantityTextView.text = "Total Quantity : $totalQuantity"
    }
}