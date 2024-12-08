package com.example.uts_empat_cina_map.AdminFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.uts_empat_cina_map.AdminItemFragment
import com.example.uts_empat_cina_map.CheckoutFragment
import com.example.uts_empat_cina_map.HomeFragment
import com.example.uts_empat_cina_map.OrderData.CartManager
import com.example.uts_empat_cina_map.OrderData.FoodItem
import com.example.uts_empat_cina_map.R

class AdminFoodItemDetailFragment : Fragment() {

    private lateinit var foodImage: ImageView
    private lateinit var foodName: TextView
    private lateinit var foodDescription: TextView
    private lateinit var foodPrice: TextView
    private lateinit var quantityInput: EditText
    private lateinit var addToCartButton: Button
    private lateinit var checkoutButton: Button

    private var foodItem: FoodItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_food_item_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        foodImage = view.findViewById(R.id.foodImage)
        foodName = view.findViewById(R.id.foodName)
        foodDescription = view.findViewById(R.id.foodDescription)
        foodPrice = view.findViewById(R.id.foodPrice)
        quantityInput = view.findViewById(R.id.quantityInput)
        addToCartButton = view.findViewById(R.id.addToCartButton)
        val decrementButton: Button = view.findViewById(R.id.decrementButton)
        val incrementButton: Button = view.findViewById(R.id.incrementButton)
        val backButton: Button = view.findViewById(R.id.backButton)

        // Retrieve the food item from the arguments
        foodItem = arguments?.getParcelable("food_item")

        // Populate UI with food item details
        foodItem?.let {
            foodName.text = it.name
            foodDescription.text = it.description
            foodPrice.text = "$${it.price}/pcs"
            Glide.with(this).load(it.imageUrl).into(foodImage) // Use imageUrl for Glide
        }

        // Set click listener for adding item to cart
        addToCartButton.setOnClickListener {
            val quantity = quantityInput.text.toString().toIntOrNull() ?: 1
            // Add the item to the cart with the specified quantity
            foodItem?.let { item ->
                CartManager.addItemToCart(item, quantity)
                Toast.makeText(context, "${item.name} added to cart", Toast.LENGTH_SHORT).show()
            }
        }
        // Increment button click listener
        incrementButton.setOnClickListener {
            val currentQuantity = quantityInput.text.toString().toIntOrNull() ?: 1
            quantityInput.setText((currentQuantity + 1).toString())
        }

        // Decrement button click listener
        decrementButton.setOnClickListener {
            val currentQuantity = quantityInput.text.toString().toIntOrNull() ?: 1
            if (currentQuantity > 1) {
                quantityInput.setText((currentQuantity - 1).toString())
            }
        }
        backButton.setOnClickListener {
            // Navigate to HomeFragment
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AdminItemFragment()) // Replace with your container ID
                .addToBackStack(null) // Optional, to add to the back stack
                .commit()
        }
    }
}
