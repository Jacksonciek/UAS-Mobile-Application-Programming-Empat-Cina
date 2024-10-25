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

class AllFoodFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodAdapter: FoodItemAdapter
    private val foodItemList = mutableListOf<FoodItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_food, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        foodAdapter = FoodItemAdapter(requireContext(), foodItemList)
        recyclerView.adapter = foodAdapter

        fetchAllFoodItems()
        return view
    }

    private fun fetchAllFoodItems() {
        val db = FirebaseFirestore.getInstance()
        db.collection("items") // Ensure this matches the collection name in Firestore
            .get()
            .addOnSuccessListener { documents ->
                foodItemList.clear()
                for (document in documents) {
                    val foodItem = document.toObject<FoodItem>().apply {
                        // This will populate the FoodItem properties based on Firestore document fields
                        // Ensure that the Firestore document structure matches FoodItem class
                    }
                    foodItemList.add(foodItem)
                }
                foodAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle the error
                e.printStackTrace() // Log the error for debugging
            }
    }
}
