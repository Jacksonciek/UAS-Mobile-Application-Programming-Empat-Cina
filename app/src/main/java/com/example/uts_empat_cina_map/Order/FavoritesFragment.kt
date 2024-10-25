package com.example.uts_empat_cina_map.Order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.OrderData.FoodItem
import com.example.uts_empat_cina_map.R

class FavoritesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: FoodItemAdapter

    // List of all food items (you might want to change this to fetch from your data source)
    private val allFoodItems = listOf(
        FoodItem("Pizza",
            R.drawable.pizza, isFavorite = true, description = "Delicious cheesy pizza", price = 9.99, imageUrl = "url_to_pizza_image"),
        FoodItem("Burger",
            R.drawable.burger, isFavorite = false, description = "Juicy beef burger", price = 5.99, imageUrl = "url_to_burger_image"),
        FoodItem("Sushi",
            R.drawable.sushi, isFavorite = true, description = "Fresh sushi rolls", price = 12.99, imageUrl = "url_to_sushi_image"),
        FoodItem("Smoothie",
            R.drawable.smoothie, isFavorite = true, description = "Fruity smoothie", price = 5.49, imageUrl = "url_to_smoothie_image"),
        FoodItem("Juice",
            R.drawable.juice, isFavorite = false, description = "Freshly squeezed juice", price = 3.49, imageUrl = "url_to_juice_image"),
        FoodItem("Babyoil",
            R.drawable.babyoil, isFavorite = false, description = "Soothing baby oil", price = 4.99, imageUrl = "url_to_babyoil_image")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // Filter favorite items from the allFoodItems list
        val favoriteItems = allFoodItems.filter { it.isFavorite }

        // Initialize the adapter with the favorite items
        foodAdapter = FoodItemAdapter(requireContext(), favoriteItems)

        // Set the adapter to the RecyclerView
        recyclerView.adapter = foodAdapter

        return view
    }
}
