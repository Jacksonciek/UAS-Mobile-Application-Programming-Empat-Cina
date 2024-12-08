package com.example.uts_empat_cina_map

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.OrderData.OrderAdapter
import com.example.uts_empat_cina_map.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var orderAdapter: OrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val buttonNotification: ImageView = view.findViewById(R.id.notification_redirect)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Get the current user's ID from FirebaseAuth
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid
        Log.d("HistoryFragment", "User ID: $userId")

        fetchOrderHistory(userId.toString())


        buttonNotification.setOnClickListener {
            val fragment = notification()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }

    private fun fetchOrderHistory(userId: String?) {
        if (userId.isNullOrEmpty()) {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firestore", "Successfully retrieved ${documents.size()} documents.")
                val orders = mutableListOf<Order>()
                for (document in documents) {
                    val order = document.toObject(Order::class.java)
                    orders.add(order)
                }
                displayOrders(orders)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents", exception)
                Toast.makeText(context, "Failed to load orders: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

    }


    private fun displayOrders(orders: List<Order>) {
        // Set up the RecyclerView
        recyclerView = view?.findViewById(R.id.recyclerViewHistory) ?: return
        orderAdapter = OrderAdapter(orders)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = orderAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}