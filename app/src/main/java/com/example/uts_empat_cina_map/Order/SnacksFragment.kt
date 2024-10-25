package com.example.uts_empat_cina_map.Order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.OrderData.FoodItem
import com.example.uts_empat_cina_map.R
import com.google.firebase.firestore.FirebaseFirestore

class SnacksFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var foodItemAdapter: FoodItemAdapter
    private val foodItemList: MutableList<FoodItem> = mutableListOf()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_snacks, container, false)
        recyclerView = view.findViewById(R.id.recyclerView) // Assuming you have a RecyclerView with this ID
        foodItemAdapter = FoodItemAdapter(requireContext(), foodItemList)
        recyclerView.adapter = foodItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        fetchSnacksItems()

        return view
    }

    private fun fetchSnacksItems() {
        firestore.collection("items")
            .whereEqualTo("category", "Snacks") // Query for Snacks items
            .get()
            .addOnSuccessListener { documents ->
                foodItemList.clear()
                for (document in documents) {
                    val foodItem = document.toObject(FoodItem::class.java)
                    foodItemList.add(foodItem)
                }
                foodItemAdapter.updateData(foodItemList) // Update the adapter with new data
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }
}
