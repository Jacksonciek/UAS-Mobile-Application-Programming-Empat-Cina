package com.example.uts_empat_cina_map

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.adapter.AdminItemAdapter
import com.example.uts_empat_cina_map.models.FoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminItemFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adminItemAdapter: AdminItemAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val itemList = mutableListOf<FoodItem>()
//    private val itemList = mutableListOf(
//        FoodItem("Burger", 10),
//        FoodItem("Pizza", 15),
//        FoodItem("Pasta", 20),
//        FoodItem("Fries", 25),
//        FoodItem("Soda", 30),
//    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_item, container, false)

        searchEditText = view.findViewById(R.id.searchEditText)
        recyclerView = view.findViewById(R.id.recyclerView)

        fetchDataFirestore()

        // Set up RecyclerView with the adapter
        adminItemAdapter = AdminItemAdapter(itemList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adminItemAdapter

        // Add search functionality
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                adminItemAdapter.filter(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Check if the user is signed in and get the email
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in
            val email = currentUser.email
            Log.d(TAG, "Current user email: $email")  // Logs the email of the current user
        } else {
            // No user is signed in
            Log.d(TAG, "No user is signed in")
        }

        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        // Fetch the food items after the view is created
//        fetchDataFirestore()
//    }

    private fun fetchDataFirestore() {
        // Fetch data from Firestore
        firestore.collection("items")
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val stock = document.getLong("stock")?.toInt() ?: 0
                    itemList.add(FoodItem(name, stock))
                }

                // Call the filter function with an empty string to show all items
                adminItemAdapter.filter("")

                adminItemAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: ", exception)
            }
    }
}
