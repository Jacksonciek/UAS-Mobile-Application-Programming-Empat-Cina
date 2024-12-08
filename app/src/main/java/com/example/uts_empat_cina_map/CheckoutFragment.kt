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
import android.widget.TextView
import android.widget.Spinner
import com.bumptech.glide.Glide
import com.example.uts_empat_cina_map.OrderData.CartManager

// Updated CheckoutFragment.kt
class CheckoutFragment : Fragment() {

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
        val backButton: Button = view.findViewById(R.id.backButton)

        // Set up spinner with dummy payment options
        val paymentMethods = arrayOf("Credit Card", "PayPal", "Cash on Delivery")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentMethods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        paymentMethodSpinner.adapter = adapter

        // Calculate total price and quantity from cart items
        updateCheckoutDetails()

        confirmButton.setOnClickListener {
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ConfirmPaymentFragment())
                .addToBackStack(null)
                .commit()
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

            // Inflate item_order.xml for each item
            val itemLayout = layoutInflater.inflate(R.layout.item_order, itemListLayout, false)

            // Bind data to the views in item_order.xml
            val foodImageView = itemLayout.findViewById<ImageView>(R.id.foodImageView)
            val foodNameTextView = itemLayout.findViewById<TextView>(R.id.foodNameTextView)
            val foodPriceTextView = itemLayout.findViewById<TextView>(R.id.foodPriceTextView)
            val foodQuantityTextView = itemLayout.findViewById<TextView>(R.id.foodQuantityTextView)
            val deleteButton = itemLayout.findViewById<Button>(R.id.deleteButton)
            val increaseQuantityButton = itemLayout.findViewById<Button>(R.id.increaseQuantityButton)

            // Set the data to the views
            foodNameTextView.text = cartItem.foodItem.name
            foodPriceTextView.text = "Price: $${cartItem.foodItem.price}"
            foodQuantityTextView.text = "Quantity: ${cartItem.quantity}"

            // You can set an image using a library like Glide or Picasso
            Glide.with(this).load(cartItem.foodItem.imageUrl).into(foodImageView)

            // Add item layout to the list
            itemListLayout.addView(itemLayout)

            // Set the delete button logic
            deleteButton.setOnClickListener {
                if (cartItem.quantity > 1) {
                    // Decrement quantity if greater than 1
                    cartItem.quantity -= 1
                } else {
                    // Remove item if quantity is 1 or less
                    CartManager.removeCartItem(cartItem)
                }
                // Update UI after removing or decrementing the item
                updateCheckoutDetails()
            }

            // Set the increase quantity button logic
            increaseQuantityButton.setOnClickListener {
                // Increase the quantity by 1
                cartItem.quantity += 1
                // Update UI after increasing the quantity
                updateCheckoutDetails()
            }
        }

        // Update the UI with total price and quantity
        totalPriceTextView.text = "$${String.format("%.2f", totalPrice)}"
        totalQuantityTextView.text = "Total Quantity : $totalQuantity"
    }

}
