package com.example.uts_empat_cina_map.Order

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.uts_empat_cina_map.OrderData.CartManager
import com.example.uts_empat_cina_map.OrderData.FoodItem
import com.example.uts_empat_cina_map.R
import com.google.firebase.firestore.FirebaseFirestore

class FoodItemDetailFragment : Fragment() {

    private lateinit var foodImage: ImageView
    private lateinit var foodName: TextView
    private lateinit var foodDescription: TextView
    private lateinit var foodPrice: TextView
    private lateinit var quantityInput: EditText
    private lateinit var addToCartButton: Button

    private var foodItem: FoodItem? = null
    private var availableStock: Int = 0 // Holds the stock from Firestore (or passed item)
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_item_detail, container, false)
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
            Glide.with(this).load(it.imageUrl).into(foodImage)

            // Use the stock from the passed FoodItem directly
            availableStock = it.stock
            validateQuantity()
        }

        // Validate quantity on text change
        quantityInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateQuantity()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Increment button click listener
        incrementButton.setOnClickListener {
            val currentQuantity = quantityInput.text.toString().toIntOrNull() ?: 1
            if (currentQuantity < availableStock) {
                quantityInput.setText((currentQuantity + 1).toString())
            } else {
                Toast.makeText(context, "Maximum stock reached", Toast.LENGTH_SHORT).show()
            }
        }

        // Decrement button click listener
        decrementButton.setOnClickListener {
            val currentQuantity = quantityInput.text.toString().toIntOrNull() ?: 1
            if (currentQuantity > 1) {
                quantityInput.setText((currentQuantity - 1).toString())
            }
        }

        // Add or update item in cart
        addToCartButton.setOnClickListener {
            val quantity = quantityInput.text.toString().toIntOrNull() ?: 1
            if (quantity <= availableStock) {
                foodItem?.let { item ->
                    CartManager.addOrUpdateCartItem(item, quantity) // Updated here
                    Toast.makeText(context, "${item.name} added to cart (Qty: $quantity)", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Quantity exceeds stock", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // Navigate back to the previous fragment
        }
    }

    private fun validateQuantity() {
        val quantity = quantityInput.text.toString().toIntOrNull() ?: 0
        when {
            quantity > availableStock -> {
                quantityInput.error = "Exceeds available stock: $availableStock"
                addToCartButton.isEnabled = false
            }
            quantity <= 0 -> {
                quantityInput.error = "Quantity must be at least 1"
                addToCartButton.isEnabled = false
            }
            else -> {
                quantityInput.error = null
                addToCartButton.isEnabled = true
            }
        }
    }
}
