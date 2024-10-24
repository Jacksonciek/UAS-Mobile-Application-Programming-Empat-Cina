package com.example.uts_empat_cina_map

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class AdminAddItemFragment : Fragment() {

    private lateinit var itemName: EditText
    private lateinit var itemLocation: EditText
    private lateinit var itemPrice: EditText
    private lateinit var stockQuantity: TextView
    private lateinit var decreaseStockButton: Button
    private lateinit var increaseStockButton: Button
    private lateinit var itemDescription: EditText
    private lateinit var uploadImageButton: Button
    private lateinit var saveButton: Button
    private lateinit var imageUri: Uri

    private var stockCount: Int = 0
    private val storage = FirebaseStorage.getInstance().reference
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_add_item, container, false)

        itemName = view.findViewById(R.id.item_name)
        itemLocation = view.findViewById(R.id.item_location)
        itemPrice = view.findViewById(R.id.item_price)
        stockQuantity = view.findViewById(R.id.stock_quantity)
        decreaseStockButton = view.findViewById(R.id.decrease_stock_button)
        increaseStockButton = view.findViewById(R.id.increase_stock_button)
        itemDescription = view.findViewById(R.id.item_description)
        uploadImageButton = view.findViewById(R.id.upload_image_button)
        saveButton = view.findViewById(R.id.save_button)

        setupStockButtons()
        setupImageUpload()
        setupSaveButton()

        return view
    }

    private fun setupStockButtons() {
        increaseStockButton.setOnClickListener {
            stockCount++
            stockQuantity.text = stockCount.toString()
        }

        decreaseStockButton.setOnClickListener {
            if (stockCount > 0) {
                stockCount--
                stockQuantity.text = stockCount.toString()
            }
        }
    }

    private fun setupImageUpload() {
        uploadImageButton.setOnClickListener {
            // Open file picker to choose an image
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                imageUri = it
                Toast.makeText(context, "Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val name = itemName.text.toString().trim()
            val location = itemLocation.text.toString().trim()
            val price = itemPrice.text.toString().trim()
            val description = itemDescription.text.toString().trim()

            if (name.isEmpty() || location.isEmpty() || price.isEmpty() || description.isEmpty() || stockCount == 0) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (this::imageUri.isInitialized) {
                uploadImageToStorage(name, location, price, description)
            } else {
                Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToStorage(name: String, location: String, price: String, description: String) {
        val imageRef = storage.child("items/${UUID.randomUUID()}.jpg")
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    uploadItemToFirestore(name, location, price, description, uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadItemToFirestore(name: String, location: String, price: String, description: String, imageUrl: String) {
        val item = hashMapOf(
            "name" to name,
            "location" to location,
            "price" to price,
            "stock" to stockCount,
            "description" to description,
            "imageUrl" to imageUrl
        )

        firestore.collection("items").add(item)
            .addOnSuccessListener {
                Toast.makeText(context, "Item successfully uploaded", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to upload item", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        itemName.text.clear()
        itemLocation.text.clear()
        itemPrice.text.clear()
        itemDescription.text.clear()
        stockCount = 0
        stockQuantity.text = stockCount.toString()
    }
}
