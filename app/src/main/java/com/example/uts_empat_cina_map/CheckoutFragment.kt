package com.example.uts_empat_cina_map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.uts_empat_cina_map.OrderData.CartManager
import com.example.uts_empat_cina_map.OrderData.FoodItem
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutFragment : Fragment() {

    private lateinit var paymentMethodSpinner: Spinner
    private lateinit var confirmButton: Button
    private lateinit var totalPriceTextView: TextView
    private lateinit var totalQuantityTextView: TextView
    private lateinit var itemListLayout: LinearLayout

    private val firestore = FirebaseFirestore.getInstance()

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

        // Update cart UI
        updateCheckoutDetails()

        // Handle Confirm Button click
        confirmButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ConfirmPaymentFragment())
                .addToBackStack(null)
                .commit()
        }

        // Handle Back Button click
        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateCheckoutDetails() {
        val cartItems = CartManager.getCartItems()

        // Reset totals
        var totalPrice = 0.0
        var totalQuantity = 0

        // Clear existing views
        itemListLayout.removeAllViews()

        // Iterate through the cart items
        for (cartItem in cartItems) {
            totalPrice += cartItem.foodItem.price * cartItem.quantity
            totalQuantity += cartItem.quantity

            // Inflate the item layout
            val itemLayout = layoutInflater.inflate(R.layout.item_order, itemListLayout, false)

            // Bind data to views
            val foodImageView = itemLayout.findViewById<ImageView>(R.id.foodImageView)
            val foodNameTextView = itemLayout.findViewById<TextView>(R.id.foodNameTextView)
            val foodPriceTextView = itemLayout.findViewById<TextView>(R.id.foodPriceTextView)
            val foodQuantityTextView = itemLayout.findViewById<TextView>(R.id.foodQuantityTextView)
            val deleteButton = itemLayout.findViewById<Button>(R.id.deleteButton)
            val increaseQuantityButton = itemLayout.findViewById<Button>(R.id.increaseQuantityButton)

            // Set item data
            foodNameTextView.text = cartItem.foodItem.name
            foodPriceTextView.text = "Price: $${cartItem.foodItem.price}"
            foodQuantityTextView.text = "Quantity: ${cartItem.quantity}"
            Glide.with(this).load(cartItem.foodItem.imageUrl).into(foodImageView)

            // Add item to the layout
            itemListLayout.addView(itemLayout)

            // Handle delete button click
            deleteButton.setOnClickListener {
                CartManager.removeCartItem(cartItem)
                updateCheckoutDetails()
            }

            // Handle increase quantity button click
            increaseQuantityButton.setOnClickListener {
                fetchStock(cartItem.foodItem.name) { availableStock ->
                    if (cartItem.quantity < availableStock) {
                        cartItem.quantity += 1
                        updateCheckoutDetails()
                    } else {
                        Toast.makeText(context, "Cannot exceed available stock", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Update total price and quantity in the UI
        totalPriceTextView.text = "$${String.format("%.2f", totalPrice)}"
        totalQuantityTextView.text = "Total Quantity: $totalQuantity"
    }

    private fun fetchStock(itemName: String, callback: (Int) -> Unit) {
        if (itemName.isEmpty()) {
            Toast.makeText(context, "Invalid item name", Toast.LENGTH_SHORT).show()
            callback(0)
            return
        }

        firestore.collection("items")
            .whereEqualTo("name", itemName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val stock = document.getLong("stock")?.toInt() ?: 0
                    callback(stock)
                } else {
                    Toast.makeText(context, "Item not found", Toast.LENGTH_SHORT).show()
                    callback(0)
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to fetch stock", Toast.LENGTH_SHORT).show()
                callback(0)
            }
    }
}
