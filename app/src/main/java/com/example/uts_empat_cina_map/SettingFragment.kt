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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SettingFragment : Fragment() {

    private lateinit var nameField: EditText
    private lateinit var emailField: TextView
    private lateinit var passwordField: EditText
    private lateinit var avatarImageView: ImageView
    private lateinit var uploadAvatarButton: Button
    private lateinit var logoutButton: Button
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageView
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var avatarUri: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        nameField = view.findViewById(R.id.nameField)
        emailField = view.findViewById(R.id.emailField)
        passwordField = view.findViewById(R.id.passwordField)
        avatarImageView = view.findViewById(R.id.avatarImageView)
        uploadAvatarButton = view.findViewById(R.id.uploadAvatarButton)
        logoutButton = view.findViewById(R.id.logoutButton)
        saveButton = view.findViewById(R.id.saveButton)
        backButton = view.findViewById(R.id.backButton) // Added back button

        loadUserInfo()
        setupSaveButton()

        uploadAvatarButton.setOnClickListener { chooseImage() }
        logoutButton.setOnClickListener { logoutUser() }
        backButton.setOnClickListener { navigateToProfileFragment() } // Handle back button

        return view
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        user?.let {
            // Set the emailField to the current user's email
            emailField.text = it.email ?: "No email available"

            firestore.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nameField.setText(document.getString("name"))
                        val avatarUrl = document.getString("avatarUrl")
                        if (avatarUrl != null) {
                            // Load avatar image using your preferred image loading library
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to load user info", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val name = nameField.text.toString().trim()
            val password = passwordField.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isNotEmpty()) {
                auth.currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserData(name)
                    } else {
                        Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                saveUserData(name)
            }
        }
    }

    private fun saveUserData(name: String) {
        val userId = auth.currentUser?.uid ?: return
        val userUpdates = mutableMapOf(
            "name" to name
        )
        if (avatarUri != null) {
            uploadAvatarToStorage(userId) { avatarUrl ->
                userUpdates["avatarUrl"] = avatarUrl
                updateFirestore(userId, userUpdates)
            }
        } else {
            updateFirestore(userId, userUpdates)
        }
    }

    private fun uploadAvatarToStorage(userId: String, onSuccess: (String) -> Unit) {
        val avatarRef = storage.reference.child("avatars/$userId.jpg")
        avatarUri?.let {
            avatarRef.putFile(it).addOnSuccessListener {
                avatarRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to upload avatar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateFirestore(userId: String, userUpdates: Map<String, Any>) {
        firestore.collection("users").document(userId).update(userUpdates)
            .addOnSuccessListener {
                Toast.makeText(context, "User info updated", Toast.LENGTH_SHORT).show()
                navigateToProfileFragment() // Navigate back to ProfileFragment after saving
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update user info", Toast.LENGTH_SHORT).show()
            }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            avatarUri = data.data
            avatarImageView.setImageURI(avatarUri)
        }
    }

    private fun logoutUser() {
        auth.signOut()
        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(requireContext(), LoginActivity::class.java)) // Navigate to LoginActivity
        activity?.finish() // End current activity
    }

    private fun navigateToProfileFragment() {
        parentFragmentManager.popBackStack() // Go back to ProfileFragment
    }
}
