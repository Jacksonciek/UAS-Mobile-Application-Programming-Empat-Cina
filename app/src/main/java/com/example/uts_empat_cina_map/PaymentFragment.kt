package com.example.uts_empat_cina_map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_empat_cina_map.OrderData.PaymentMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentFragment : Fragment() {
    private lateinit var paymentRecyclerView: RecyclerView
    private lateinit var redirectAddNewPayment: Button
    private lateinit var backButton: ImageView

    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        // Initialize views
        paymentRecyclerView = view.findViewById(R.id.paymentRecyclerView)
        redirectAddNewPayment = view.findViewById(R.id.buttonAddNewPayment)
        backButton = view.findViewById(R.id.backButton)

        // Set up RecyclerView
        paymentRecyclerView.layoutManager = LinearLayoutManager(context)

        // Fetch and display payment methods
        fetchPayments()

        // Redirect to Add New Payment screen
        redirectAddNewPayment.setOnClickListener {
            val fragment = new_payment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        // Handle back button
        backButton.setOnClickListener {
            val fragment = ProfileFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return view
    }

    private fun fetchPayments() {
        currentUser?.uid?.let { userId ->
            firestore.collection("user_payments")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val paymentList = documents.map { doc ->
                        PaymentMethod(
                            paymentType = doc.getString("paymentType") ?: "",
                            labelName = doc.getString("labelName") ?: "",
                            cardNumber = doc.getString("cardNumber") ?: "",
                            description = doc.getString("description") ?: ""
                        )
                    }

                    // Set adapter to display payment methods
                    val adapter = PaymentAdapter(paymentList)
                    paymentRecyclerView.adapter = adapter
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to fetch payment methods", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
