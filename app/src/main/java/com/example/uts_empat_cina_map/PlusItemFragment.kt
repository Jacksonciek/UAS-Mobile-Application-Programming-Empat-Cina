package com.example.uts_empat_cina_map

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class PlusItemFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null // To store selected image URI
    private var stockCount: Int = 0

    // Declare views
    private lateinit var btnIncrement: Button
    private lateinit var btnDecrement: Button
    private lateinit var tvNumber: TextView
    private lateinit var buttonImage: Button
    private lateinit var saveChanges: Button
    private lateinit var insertItem: EditText
    private lateinit var expiredDateInput: EditText
    private lateinit var fulldesc: EditText

    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        return inflater.inflate(R.layout.fragment_plus_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        btnIncrement = view.findViewById(R.id.btnIncrement)
        btnDecrement = view.findViewById(R.id.btnDecrement)
        tvNumber = view.findViewById(R.id.tvNumber)
        buttonImage = view.findViewById(R.id.buttonImage)
        saveChanges = view.findViewById(R.id.saveChanges)
        insertItem = view.findViewById(R.id.insertItem)
        expiredDateInput = view.findViewById(R.id.expiredDateInput)
        fulldesc = view.findViewById(R.id.fulldesc)

        // Increment stock
        btnIncrement.setOnClickListener {
            stockCount++
            tvNumber.text = stockCount.toString()
        }

        // Decrement stock
        btnDecrement.setOnClickListener {
            if (stockCount > 0) {
                stockCount--
                tvNumber.text = stockCount.toString()
            }
        }

        // Select image
        buttonImage.setOnClickListener {
            selectImageFromGallery()
        }

        // Date picker to select expiration date
        expiredDateInput.setOnClickListener {
            showDatePickerDialog()
        }

        // Save changes
        saveChanges.setOnClickListener {
            saveItemData()
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                expiredDateInput.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            Toast.makeText(requireContext(), "Image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveItemData() {
        val itemName = insertItem.text.toString().trim()
        val expireDate = selectedDate
        val description = fulldesc.text.toString().trim()

        // Validation
        if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(description) || TextUtils.isEmpty(expireDate)) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the current userId
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a unique file path for each user
        val filePath = if (FirebaseAuth.getInstance().currentUser?.email == "admin@gmail.com") {
            // Admin uploads to the general items collection
            "items/${UUID.randomUUID()}"
        } else {
            // Regular users upload to their own "user_items" folder
            "user_items/$userId/${UUID.randomUUID()}"
        }

        // Upload image if selected
        if (imageUri != null) {
            val storageRef = storage.reference.child(filePath)
            storageRef.putFile(imageUri!!).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveToFirestore(itemName, expireDate, description, uri.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            saveToFirestore(itemName, expireDate, description, null)
        }
    }

    private fun saveToFirestore(
        itemName: String,
        expireDate: String,
        description: String,
        imageUrl: String?
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val username = FirebaseAuth.getInstance().currentUser?.displayName ?: "Unknown"

        val itemData = hashMapOf(
            "name" to itemName,
            "expireDate" to expireDate,
            "description" to description,
            "stock" to stockCount,
            "imageUrl" to imageUrl,
            "userId" to userId
        )

        // Save the item in the user's personal collection
        firestore.collection("user_items")
            .document(userId) // Using userId as the document ID
            .collection("items")
            .add(itemData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Item added successfully", Toast.LENGTH_SHORT).show()
                clearInputs()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to save item", Toast.LENGTH_SHORT).show()
            }
    }


    private fun clearInputs() {
        insertItem.text.clear()
        expiredDateInput.text.clear()
        fulldesc.text.clear()
        tvNumber.text = "0"
        stockCount = 0
        imageUri = null
    }
}
