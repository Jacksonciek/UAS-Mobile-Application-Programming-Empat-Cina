package com.example.uts_empat_cina_map

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream
import java.util.*

class PlusItemFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
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
    private lateinit var locationInput: EditText // New Location Input Field
    private lateinit var uploadedImageView: ImageView

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
        locationInput = view.findViewById(R.id.locationInput) // Initialize Location Input
        uploadedImageView = view.findViewById(R.id.uploaded_image_view)

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

        setupImageUpload()

        // Date picker to select expiration date
        expiredDateInput.setOnClickListener {
            showDatePickerDialog()
        }

        // Save changes
        saveChanges.setOnClickListener {
            saveItemData()
        }
    }

    private fun setupImageUpload() {
        buttonImage.setOnClickListener {
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
                        buttonImage.visibility = View.GONE
                    }
                }
                GALLERY_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        imageUri = uri
                        // Ensure imageUri is non-null before using it
                        imageUri?.let { nonNullImageUri ->
                            val bitmap = decodeSampledBitmapFromUri(nonNullImageUri, 300, 300)
                            uploadedImageView.setImageBitmap(bitmap)
                            uploadedImageView.visibility = View.VISIBLE
                            Toast.makeText(context, "Image Selected", Toast.LENGTH_SHORT).show()
                            buttonImage.visibility = View.GONE
                        }
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

    companion object {
        private const val CAMERA_REQUEST_CODE = 2000
        private const val GALLERY_REQUEST_CODE = 1000
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val path = android.os.Environment.getExternalStorageDirectory().toString() + "/image.jpg"
        val file = File(path)
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        return Uri.fromFile(file)
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

    private fun saveItemData() {
        val itemName = insertItem.text.toString().trim()
        val expireDate = selectedDate
        val description = fulldesc.text.toString().trim()
        val location = locationInput.text.toString().trim() // Get location input

        // Validation for required fields
        if (TextUtils.isEmpty(itemName) || TextUtils.isEmpty(description) ||
            TextUtils.isEmpty(expireDate) || TextUtils.isEmpty(location)) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri == null) {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the current userId
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val filePath = if (FirebaseAuth.getInstance().currentUser?.email == "admin@gmail.com") {
            "items/${UUID.randomUUID()}"
        } else {
            "user_items/$userId/${UUID.randomUUID()}"
        }

        // Upload image if selected
        val storageRef = storage.reference.child(filePath)
        storageRef.putFile(imageUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                saveToFirestore(itemName, expireDate, description, uri.toString(), location)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToFirestore(
        itemName: String,
        expireDate: String,
        description: String,
        imageUrl: String?,
        location: String
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(requireContext(), "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val itemData = hashMapOf(
            "name" to itemName,
            "expireDate" to expireDate,
            "description" to description,
            "stock" to stockCount,
            "imageUrl" to imageUrl,
            "location" to location, // Add location to Firestore data
            "userId" to userId
        )

        firestore.collection("user_items")
            .document(userId)
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
        locationInput.text.clear() // Clear location input
        tvNumber.text = "0"
        stockCount = 0
        imageUri = null
    }
}
