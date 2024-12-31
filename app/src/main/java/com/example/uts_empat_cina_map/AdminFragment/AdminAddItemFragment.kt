package com.example.uts_empat_cina_map

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
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
    private lateinit var itemCategorySpinner: Spinner
    private lateinit var uploadedImageView: ImageView

    private var stockCount: Int = 0
    private val storage = FirebaseStorage.getInstance().reference
    private val firestore = FirebaseFirestore.getInstance()

    private fun setupSpinner() {
        // Example categories
        val categories = listOf("Choose Category", "Appetizer", "Main Course", "Dessert", "Beverage")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        itemCategorySpinner.adapter = adapter
    }

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
        uploadedImageView = view.findViewById(R.id.uploaded_image_view)
        uploadImageButton = view.findViewById(R.id.upload_image_button)
        saveButton = view.findViewById(R.id.save_button)
        itemCategorySpinner = view.findViewById(R.id.item_category_spinner)

        setupSpinner() // Initialize spinner
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
            // Show dialog to choose between Camera and Gallery
            val options = arrayOf("Take Photo", "Choose from Gallery")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select Image Source")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera() // Take photo
                    1 -> openGallery() // Choose from gallery
                }
            }
            builder.show()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    private fun decodeSampledBitmapFromUri(uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri), null, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri), null, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        // Save the bitmap to a file and set `imageUri`
                        val tempUri = saveBitmapToFile(it)
                        imageUri = tempUri // Set imageUri for further use
                        uploadedImageView.setImageBitmap(it)
                        uploadedImageView.visibility = View.VISIBLE
                        Toast.makeText(context, "Image Captured", Toast.LENGTH_SHORT).show()
                        uploadImageButton.visibility = View.GONE
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    data?.data?.let {
                        imageUri = it
                        val bitmap = decodeSampledBitmapFromUri(imageUri, 300, 300)
                        uploadedImageView.setImageBitmap(bitmap)
                        uploadedImageView.visibility = View.VISIBLE
                        Toast.makeText(context, "Image Selected", Toast.LENGTH_SHORT).show()
                        uploadImageButton.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Uri {
        val file = File(requireContext().cacheDir, "temp_image.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return Uri.fromFile(file)
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val name = itemName.text.toString().trim()
            val location = itemLocation.text.toString().trim()
            val priceString = itemPrice.text.toString().trim()
            val description = itemDescription.text.toString().trim()
            val category = itemCategorySpinner.selectedItem.toString()

            // Check if any fields are empty or if "Choose Category" is selected
            if (name.isEmpty() || location.isEmpty() || priceString.isEmpty() ||
                description.isEmpty() || stockCount == 0 ||
                category == getString(R.string.select_category_prompt)) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert price from String to Double
            val price = priceString.toDoubleOrNull()
            if (price == null) {
                Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (this::imageUri.isInitialized && imageUri.toString().isNotEmpty()) {
                uploadImageToStorage(name, location, price, description, category)
            } else {
                Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 2000
        private const val GALLERY_REQUEST_CODE = 1000
    }

    private fun uploadImageToStorage(name: String, location: String, price: Double, description: String, category: String) {
        val imageRef = storage.child("items/${UUID.randomUUID()}.jpg")
        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    uploadItemToFirestore(name, location, price, description, category, uri.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadItemToFirestore(name: String, location: String, price: Double, description: String, category: String, imageUrl: String) {
        val item = hashMapOf(
            "name" to name,
            "location" to location,
            "price" to price,
            "stock" to stockCount,
            "description" to description,
            "category" to category,
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
        itemCategorySpinner.setSelection(0)
        uploadedImageView.visibility = View.GONE
        uploadImageButton.visibility = View.VISIBLE
        if (this::imageUri.isInitialized) {
            imageUri = Uri.EMPTY // Reset imageUri
        }
    }
}
