package com.example.uts_empat_cina_map.Order

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.OrderData.FoodItem
import com.example.uts_empat_cina_map.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

interface SearchableFragment {
    fun filterData(query: String)
}

class AllFoodFragment : Fragment(), SearchableFragment {

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: FoodItemAdapter
    private val foodItemList = mutableListOf<FoodItem>()
    private val allFoodItems = mutableListOf<FoodItem>() // Store all food items to filter later

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the fragment
        val view = inflater.inflate(R.layout.fragment_all_food, container, false)

        // Set up RecyclerView and adapter
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        foodAdapter = FoodItemAdapter(requireContext(), foodItemList)
        recyclerView.adapter = foodAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Fetch the food items after the view is created
        fetchAllFoodItems()
    }

    private fun fetchAllFoodItems() {
        val db = FirebaseFirestore.getInstance()
        db.collection("items")
            .get()
            .addOnSuccessListener { documents ->
                foodItemList.clear()
                allFoodItems.clear()
                for (document in documents) {
                    val foodItem = document.toObject<FoodItem>()
                    allFoodItems.add(foodItem)
                    foodItemList.add(foodItem)
                }
                foodAdapter.notifyDataSetChanged()  // Refresh the RecyclerView with new data
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }

    override fun filterData(query: String) {
        if (query.isEmpty()) {
            foodItemList.clear()
            foodItemList.addAll(allFoodItems)
        } else {
            foodItemList.clear()
            foodItemList.addAll(allFoodItems.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            })
        }
        foodAdapter.notifyDataSetChanged()
    }
}
