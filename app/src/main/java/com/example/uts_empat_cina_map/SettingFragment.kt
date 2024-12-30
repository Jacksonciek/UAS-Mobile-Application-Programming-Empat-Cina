package com.example.uts_empat_cina_map

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SettingFragment : Fragment() {

    private lateinit var nameField: EditText
    private lateinit var phoneField: EditText
    private lateinit var passwordField: EditText
    private lateinit var avatarImageView: ImageView
    private lateinit var uploadAvatarButton: Button
    private lateinit var logoutButton: Button
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageView
    private lateinit var iconRightLogOut: ImageView
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

        // Initialize Views
        nameField = view.findViewById(R.id.nameField)
        phoneField = view.findViewById(R.id.phoneField)
        passwordField = view.findViewById(R.id.passwordField)
        avatarImageView = view.findViewById(R.id.avatarImageView)
        uploadAvatarButton = view.findViewById(R.id.uploadAvatarButton)
        logoutButton = view.findViewById(R.id.logoutButton)
        saveButton = view.findViewById(R.id.saveButton)
        backButton = view.findViewById(R.id.backButton)
        iconRightLogOut = view.findViewById(R.id.iconRightLogOut)

        // Load user information
        loadUserInfo()

        // Set up buttons
        setupSaveButton()
        uploadAvatarButton.setOnClickListener { chooseImage() }
        logoutButton.setOnClickListener { logoutUser() }
        backButton.setOnClickListener { navigateToProfileFragment() }
        iconRightLogOut.setOnClickListener { logoutUser() }

        return view
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        user?.let {
            firestore.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        nameField.setText(document.getString("name") ?: "")
                        phoneField.setText(document.getString("phone") ?: "")
                        val avatarUrl = document.getString("avatarUrl")
                        if (avatarUrl != null) {
                            Glide.with(this)
                                .load(avatarUrl)
                                .placeholder(R.drawable.glen)
                                .into(avatarImageView)
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
            val phone = phoneField.text.toString().trim()
            val password = passwordField.text.toString()

            if (name.isNotEmpty()) saveUserData(name, phone)
            if (password.isNotEmpty()) updatePassword(password)
            avatarUri?.let { uri -> uploadAvatarToStorage(auth.currentUser?.uid ?: return@setOnClickListener, uri) }
        }
    }

    private fun updatePassword(password: String) {
        auth.currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(name: String, phone: String) {
        val userId = auth.currentUser?.uid ?: return
        val userUpdates = mapOf("name" to name, "phone" to phone)
        firestore.collection("users").document(userId).update(userUpdates)
            .addOnSuccessListener {
                Toast.makeText(context, "User info updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update user info", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadAvatarToStorage(userId: String, uri: Uri) {
        val avatarRef = storage.reference.child("avatars/$userId.jpg")
        avatarRef.putFile(uri)
            .addOnSuccessListener {
                avatarRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val avatarUrl = downloadUri.toString()
                    firestore.collection("users").document(userId).update("avatarUrl", avatarUrl)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Avatar updated successfully", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to upload avatar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            avatarUri = data.data
            avatarImageView.setImageURI(avatarUri)
        }
    }

    private fun logoutUser() {
        auth.signOut()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        activity?.finish()
    }

    private fun navigateToProfileFragment() {
        parentFragmentManager.popBackStack()
    }
}
