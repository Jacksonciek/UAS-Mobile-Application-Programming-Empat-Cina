package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.Order.OrderFragment
import com.example.uts_empat_cina_map.adapter.HomeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val itemsList = mutableListOf<HomeItem>()
    private lateinit var greetings: TextView

    private var isValueHidden = true
    private val actualValue = "Rp 500.367,34"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Views initialization
        val cartButton: Button = view.findViewById(R.id.cart_button)
        val notificationButton: Button = view.findViewById(R.id.mail_button)
        val seeAll: TextView = view.findViewById(R.id.seeAll)
        val imageView6: ImageView = view.findViewById(R.id.imageView6)
        greetings = view.findViewById(R.id.textView9)

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Initialize adapter with empty list
        adapter = HomeAdapter()
        recyclerView.adapter = adapter

        // Fetch data from Firestore
        fetchDataFromFirestore()

        // Handle button clicks for navigation
        cartButton.setOnClickListener {
            navigateToFragment(CheckoutFragment())
        }

        notificationButton.setOnClickListener {
            navigateToFragment(notification())  // Make sure this fragment is defined
        }

        seeAll.setOnClickListener {
            val intent = Intent(activity, SeeAllActivity::class.java)
            startActivity(intent)
        }

        // Image click listener for navigating to OrderFragment
        imageView6.setOnClickListener {
            navigateToFragment(OrderFragment())
        }

        return view
    }

    private fun navigateToFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun fetchDataFromFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        Log.d("HomeFragment", "Fetching data for userId: $userId")

        // Fetch user name for greeting
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                val name = document.getString("name") ?: "No name available"
                greetings.text = "Hello, $name"  // Set greeting with user's name
            }

        // Fetch items data for user
        firestore.collection("user_items")
            .document(userId)
            .collection("items")
            .get()
            .addOnSuccessListener { documents ->
                itemsList.clear()

                if (documents.isEmpty) {
                    Log.d("HomeFragment", "No documents found.")
                }

                for (document in documents) {
                    val name = document.getString("name") ?: "No Name"
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val expiredDateStr = document.getString("expireDate")
                    val daysLeft = calculateDaysLeft(expiredDateStr)

                    // Only add items that have not expired
                    if (daysLeft >= 0) {
                        val homeItem = HomeItem(name, imageUrl, daysLeft)
                        itemsList.add(homeItem)
                        Log.d("HomeFragment", "Fetched item: $homeItem")  // Log each item as it is fetched
                    } else {
                        Log.d("HomeFragment", "Item expired: $name")
                    }
                }

                // Log the size of itemsList to check if data is being fetched
                Log.d("HomeFragment", "Fetched items count: ${itemsList.size}")

                // Sort items by days left in descending order
                itemsList.sortByDescending { it.daysLeft }

                // Update the adapter with new data
                adapter.updateItems(itemsList)

                // Log the updated list
                Log.d("HomeFragment", "Updated items: $itemsList")
            }
            .addOnFailureListener { e ->
                Log.e("HomeFragment", "Failed to fetch data: ${e.message}")
                Toast.makeText(requireContext(), "Failed to fetch data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun calculateDaysLeft(expiredDateStr: String?): Int {
        if (expiredDateStr.isNullOrEmpty()) return -1
        return try {
            // Modify the date format to match "dd/MM/yyyy"
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val expiredDate = sdf.parse(expiredDateStr)
            val currentDate = Date()
            val diff = expiredDate.time - currentDate.time
            (diff / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            // Log the error if parsing fails
            Log.e("HomeFragment", "Error calculating days left: ${e.message}")
            -1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incomeValueTextView: TextView = view.findViewById(R.id.incomeValue)
        val toggleButton: Button = view.findViewById(R.id.toggleButton)

        toggleButton.setOnClickListener {
            if (isValueHidden) {
                incomeValueTextView.text = actualValue
                toggleButton.setBackgroundResource(R.drawable.eye_open)
            } else {
                incomeValueTextView.text = "*************"
                toggleButton.setBackgroundResource(R.drawable.eye_closed)
            }
            isValueHidden = !isValueHidden
        }
    }
}

data class HomeItem(
    val name: String,
    val photo: String,  // Ensure this matches your model
    val daysLeft: Int
)
