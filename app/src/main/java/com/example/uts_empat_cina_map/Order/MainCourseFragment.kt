package com.example.uts_empat_cina_map.Order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.OrderData.FoodItem
import com.example.uts_empat_cina_map.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainCourseFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodItemAdapter
    private var foodItemList: MutableList<FoodItem> = mutableListOf() // Use a mutable list for updates
    private val firestore: FirebaseFirestore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_course, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = FoodItemAdapter(requireContext(), foodItemList)
        recyclerView.adapter = adapter

        fetchMainCourseItems()

        return view
    }

    private fun fetchMainCourseItems() {
        firestore.collection("items")
            .whereEqualTo("category", "Main Course") // Fetch only Main Course items
            .get()
            .addOnSuccessListener { documents ->
                foodItemList.clear() // Clear previous items
                for (document in documents) {
                    val foodItem = document.toObject(FoodItem::class.java)
                    foodItemList.add(foodItem) // Add new items to the list
                }
                adapter.updateData(foodItemList) // Update adapter with the new data
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error fetching items: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
