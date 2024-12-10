package com.example.uts_empat_cina_map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class AdminStatsFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var totalProfitTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment (fragment_admin_stats.xml)
        val view = inflater.inflate(R.layout.fragment_admin_stats, container, false)

        totalProfitTextView = view.findViewById(R.id.totalProfitTextView)
        firestore = FirebaseFirestore.getInstance()

        fetchTotalProfit()

        return view
    }

    private fun fetchTotalProfit() {
        firestore.collection("orders")
            .get()
            .addOnSuccessListener { querySnapshot ->
                var totalProfit = 0.0
                for (document in querySnapshot) {
                    val totalPrice = document.getDouble("totalPrice") ?: 0.0
                    totalProfit += totalPrice
                }
                totalProfitTextView.text = "Total Profit: Rp.$totalProfit"
            }
            .addOnFailureListener { exception ->
                Log.e("AdminStatsFragment", "Error fetching data", exception)
                totalProfitTextView.text = "Error fetching data"
            }
    }
}
