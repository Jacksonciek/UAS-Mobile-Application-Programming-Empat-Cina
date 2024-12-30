// Update AdminSettingFragment.kt
package com.example.uts_empat_cina_map.AdminFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uts_empat_cina_map.AdminProfileFragment
import com.example.uts_empat_cina_map.LoginActivity
import com.example.uts_empat_cina_map.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AdminSettingFragment : Fragment() {

    private lateinit var passwordField: EditText
    private lateinit var togglePasswordVisibility: ImageView
    private var isPasswordVisible = false
    private lateinit var logoutButton: Button
    private lateinit var backButton: ImageView
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var avatarUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_setting, container, false)
        logoutButton = view.findViewById(R.id.logoutButton)
        backButton = view.findViewById(R.id.backButton)
        passwordField = view.findViewById(R.id.passwordField)
        togglePasswordVisibility = view.findViewById(R.id.togglePasswordVisibility)

        // Toggle password visibility
        togglePasswordVisibility.setOnClickListener {
            if (isPasswordVisible) {
                passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordVisibility.setImageResource(R.drawable.eye_closed2)
            } else {
                passwordField.inputType = InputType.TYPE_CLASS_TEXT
                togglePasswordVisibility.setImageResource(R.drawable.eye_open2)
            }
            isPasswordVisible = !isPasswordVisible
            passwordField.setSelection(passwordField.text.length) // Keep cursor at the end
        }
        backButton = view.findViewById(R.id.backButton)

        backButton.setOnClickListener { navigateToAdminProfileFragment() }

        return view
    }
    private fun logoutUser() {
        auth.signOut()
        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(requireContext(), LoginActivity::class.java)) // Navigate to LoginActivity
        activity?.finish() // End current activity
    }
    private fun navigateToAdminProfileFragment() {
        val fragment = AdminProfileFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

//package com.example.uts_empat_cina_map.AdminFragment
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.uts_empat_cina_map.LoginActivity
//import com.example.uts_empat_cina_map.R
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.storage.FirebaseStorage
//
//class AdminSettingFragment  : Fragment() {
//
//    private lateinit var nameField: EditText
//    private lateinit var emailField: TextView
//    private lateinit var phoneField: EditText
//    private lateinit var passwordField: EditText
//    private lateinit var avatarImageView: ImageView
//    private lateinit var uploadAvatarButton: Button
//    private lateinit var saveButton: Button
//    private lateinit var logoutButton: Button
//    private lateinit var backButton: ImageView
//    private lateinit var iconRightLogOut: ImageView // Added iconRightLogOut
//    private val auth = FirebaseAuth.getInstance()
//    private val firestore = FirebaseFirestore.getInstance()
//    private val storage = FirebaseStorage.getInstance()
//    private var avatarUri: Uri? = null
//
//    companion object {
//        const val PICK_IMAGE_REQUEST = 1
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_admin_setting, container, false)
//
//        nameField = view.findViewById(R.id.nameField)
////        emailField = view.findViewById(R.id.emailField)
//        phoneField = view.findViewById(R.id.phoneField)
//        passwordField = view.findViewById(R.id.passwordField)
//        avatarImageView = view.findViewById(R.id.avatarImageView)
//        uploadAvatarButton = view.findViewById(R.id.uploadAvatarButton)
//        saveButton = view.findViewById(R.id.saveButton)
//        logoutButton = view.findViewById(R.id.logoutButton)
//        backButton = view.findViewById(R.id.backButton)
//        iconRightLogOut = view.findViewById(R.id.iconRightLogOut) // Initialize iconRightLogOut
//
//        loadUserInfo()
//        setupSaveButton()
//
//        uploadAvatarButton.setOnClickListener { chooseImage() }
//        logoutButton.setOnClickListener { logoutUser() }
//        backButton.setOnClickListener { navigateToProfileFragment() }
//        iconRightLogOut.setOnClickListener { logoutUser() } // Set OnClickListener for iconRightLogOut
//
//        return view
//    }
//
//    private fun loadUserInfo() {
//        val user = auth.currentUser
//        user?.let {
//            // Set the emailField to the current user's email
//            emailField.text = it.email ?: "No email available"
//
//            firestore.collection("users").document(it.uid).get()
//                .addOnSuccessListener { document ->
//                    if (document.exists()) {
//                        nameField.setText(document.getString("name"))
//                        phoneField.setText(document.getString("phone")) // Load phone number
//                        val avatarUrl = document.getString("avatarUrl")
//                        if (avatarUrl != null) {
//                            // Load avatar image using your preferred image loading library
//                        }
//                    }
//                }
//                .addOnFailureListener {
//                    Toast.makeText(context, "Failed to load user info", Toast.LENGTH_SHORT).show()
//                }
//        }
//    }
//
//    private fun setupSaveButton() {
//        saveButton.setOnClickListener {
//            val name = nameField.text.toString().trim()
//            val phone = phoneField.text.toString().trim() // Get phone number
//            val password = passwordField.text.toString()
//
//            // Update name if it's provided
//            if (name.isNotEmpty()) {
//                saveUserData(name, phone) // Pass phone number to saveUserData
//            }
//
//            // Update password if it's provided
//            if (password.isNotEmpty()) {
//                updatePassword(password)
//            }
//
//            // If avatarUri is set, upload avatar
//            avatarUri?.let { uri ->
//                val userId = auth.currentUser?.uid
//                if (userId != null) {
//                    uploadAvatarToStorage(userId, uri)
//                }
//            }
//        }
//    }
//
//    private fun updatePassword(password: String) {
//        auth.currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun saveUserData(name: String, phone: String) { // Added phone parameter
//        val userId = auth.currentUser?.uid ?: return
//        val userUpdates = mapOf("name" to name, "phone" to phone) // Include phone in updates
//
//        firestore.collection("users").document(userId).update(userUpdates)
//            .addOnSuccessListener {
//                Toast.makeText(context, "User info updated successfully", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(context, "Failed to update user info", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun uploadAvatarToStorage(userId: String, uri: Uri) {
//        Log.d("AdminSettingFragment", "Uploading avatar for user ID: $userId with URI: $uri")
//        val avatarRef = storage.reference.child("avatars/$userId.jpg")
//        avatarRef.putFile(uri)
//            .addOnSuccessListener {
//                avatarRef.downloadUrl.addOnSuccessListener { downloadUri ->
//                    val avatarUrl = downloadUri.toString()
//                    updateFirestore(userId, mapOf("avatarUrl" to avatarUrl))
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e("AdminSettingFragment", "Failed to upload avatar: ${exception.message}")
//                Toast.makeText(context, "Failed to upload avatar", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun updateFirestore(userId: String, userUpdates: Map<String, Any>) {
//        firestore.collection("users").document(userId).update(userUpdates)
//            .addOnSuccessListener {
//                Toast.makeText(context, "User info updated", Toast.LENGTH_SHORT).show()
//                navigateToProfileFragment() // Navigate back to ProfileFragment after saving
//            }
//            .addOnFailureListener {
//                Toast.makeText(context, "Failed to update user info", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    private fun chooseImage() {
//        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
//            type = "image/*"
//        }
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//            avatarUri = data.data
//            avatarImageView.setImageURI(avatarUri)
//        }
//    }
//
//    private fun logoutUser() {
//        auth.signOut()
//        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
//        startActivity(Intent(requireContext(), LoginActivity::class.java)) // Navigate to LoginActivity
//        activity?.finish() // End current activity
//    }
//
//    private fun navigateToProfileFragment() {
//        parentFragmentManager.popBackStack() // Go back to ProfileFragment
//    }
//}
