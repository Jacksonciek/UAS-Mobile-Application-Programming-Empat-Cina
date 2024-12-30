package com.example.uts_empat_cina_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class new_payment : Fragment() {

    private lateinit var paymentOptionsGroup: RadioGroup
    private lateinit var cardNumberEditText: EditText
    private lateinit var labelNameEditText: EditText
    private lateinit var cardHolderEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var doneButton: Button

    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind views
        paymentOptionsGroup = view.findViewById(R.id.paymentOptionsGroup)
        cardNumberEditText = view.findViewById(R.id.cardNumberEditText)
        labelNameEditText = view.findViewById(R.id.labelNameEditText)
        cardHolderEditText = view.findViewById(R.id.cardHolderEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        doneButton = view.findViewById(R.id.doneButton)

        doneButton.setOnClickListener {
            savePaymentMethod()
        }
    }

    private fun savePaymentMethod() {
        // Get selected payment type
        val selectedOptionId = paymentOptionsGroup.checkedRadioButtonId
        val selectedPaymentMethod = view?.findViewById<RadioButton>(selectedOptionId)?.text.toString()

        // Get user inputs
        val cardNumber = cardNumberEditText.text.toString()
        val labelName = labelNameEditText.text.toString()
        val cardHolder = cardHolderEditText.text.toString()
        val description = descriptionEditText.text.toString()

        // Validate inputs
        if (cardNumber.isEmpty() || cardHolder.isEmpty()) {
            Toast.makeText(context, "Card Number and Card Holder are required", Toast.LENGTH_SHORT).show()
            return
        }

        // Save to Firebase
        val paymentData = hashMapOf(
            "userId" to (currentUser?.uid ?: "unknown"),
            "paymentType" to selectedPaymentMethod,
            "cardNumber" to cardNumber,
            "labelName" to labelName,
            "cardHolder" to cardHolder,
            "description" to description
        )

        firestore.collection("user_payments")
            .add(paymentData)
            .addOnSuccessListener {
                Toast.makeText(context, "Payment method saved successfully!", Toast.LENGTH_SHORT).show()
                // Navigate back or clear inputs
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to save payment method", Toast.LENGTH_SHORT).show()
            }
    }
}
