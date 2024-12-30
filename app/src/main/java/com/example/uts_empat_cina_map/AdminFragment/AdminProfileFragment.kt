package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminProfileFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var profileImage: ImageView
    private lateinit var iconRightLogOut: ImageView
    private lateinit var buttonLogOut: Button
    private lateinit var passwordField: TextView
    private lateinit var togglePasswordVisibility: ImageView
    private var isPasswordVisible: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_profile, container, false)

        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize views
        profileImage = view.findViewById(R.id.profileImage)
        iconRightLogOut = view.findViewById(R.id.iconRightLogOut)
        buttonLogOut = view.findViewById(R.id.logoutButton)
        passwordField = view.findViewById(R.id.passwordField)
        togglePasswordVisibility = view.findViewById(R.id.togglePasswordVisibility)

        // Load user info
        loadUserInfo()

        // Set OnClickListener for logout button
        iconRightLogOut.setOnClickListener { logoutUser() }
        buttonLogOut.setOnClickListener { logoutUser() }
        togglePasswordVisibility.setOnClickListener { togglePasswordVisibility() }

        // Setup menu item click listeners
        setupOnClickListeners(view)

        return view
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        user?.let {
            firestore.collection("admin").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: "No name available"
                        val avatarUrl = document.getString("avatarUrl")

                        // Load avatar using Glide
                        if (!avatarUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(avatarUrl)
                                .placeholder(R.drawable.baseline_person_24)
                                .error(R.drawable.baseline_person_24)
                                .into(profileImage)
                        } else {
                            profileImage.setImageResource(R.drawable.baseline_person_24)
                        }
                    } else {
                        Log.d("AdminProfileFragment", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("AdminProfileFragment", "Error getting user document", exception)
                }
        } ?: Log.w("AdminProfileFragment", "No current user")
    }

    private fun setupOnClickListeners(view: View) {
        // Navigate to AdminReviewFragment
        val myReviewsButton: TextView = view.findViewById(R.id.myReviews)
        myReviewsButton.setOnClickListener {
            findNavController().navigate(R.id.action_adminProfileFragment_to_adminReviewFragment)
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Hide password by setting it to masked value
            passwordField.text = "*************"
            togglePasswordVisibility.setImageResource(R.drawable.eye_closed2)
        } else {
            // Show the actual password value
            passwordField.text = "admin#1234"
            togglePasswordVisibility.setImageResource(R.drawable.eye_open2)
        }
        isPasswordVisible = !isPasswordVisible
    }

    private fun logoutUser() {
        auth.signOut()
        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        activity?.finish() // End current activity
    }
}

//package com.example.uts_empat_cina_map
//
//import android.content.Intent
//import android.os.Bundle
//import android.text.Spannable
//import android.text.SpannableString
//import android.text.style.AbsoluteSizeSpan
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import com.bumptech.glide.Glide
//import com.example.uts_empat_cina_map.AdminFragment.AdminReviewFragment
//import com.example.uts_empat_cina_map.AdminFragment.AdminSettingFragment
//import com.example.uts_empat_cina_map.Order.OrderFragment
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//
//class AdminProfileFragment : Fragment() {
//
//    private lateinit var profileName: TextView
//    private lateinit var firestore: FirebaseFirestore
//    private lateinit var auth: FirebaseAuth
//
//    private lateinit var profileEmail: TextView
//    private lateinit var profilePhone: TextView
//    private lateinit var logoutButton: Button
//    private lateinit var profileImage: ImageView
//    private lateinit var iconRightLogOut: ImageView
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_admin_profile, container, false)
//
//        val buttonnotification: ImageView = view.findViewById(R.id.iconLeft)
//
//        buttonnotification.setOnClickListener {
//            val fragment = notification()
//            val fragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }
//
//        // Initialize Firebase instances
//        firestore = FirebaseFirestore.getInstance()
//        auth = FirebaseAuth.getInstance()
//
//        // Initialize views
////        profileName = view.findViewById(R.id.profileName)
////        profileEmail = view.findViewById(R.id.profileEmail)
////        profilePhone = view.findViewById(R.id.profilePhone)
//        profileImage = view.findViewById(R.id.profileImage)
//        iconRightLogOut = view.findViewById(R.id.iconRightLogOut) // Initialize iconRightLogOut
//
//        // Load user info
//        loadUserInfo()
//
//        // Set OnClickListener for iconRightLogOut
//        iconRightLogOut.setOnClickListener { logoutUser() }
//
//        // Set SpannableString for various menus
////        setSpannableString(view.findViewById(R.id.myOrders), "My Items", "Update your stocks here")
//
//        setSpannableString(view.findViewById(R.id.myReviews), "Credits", "The author of this application")
////        setSpannableString(
////            view.findViewById(R.id.settings),
////            "Settings",
////            "Admin Information (Username, Phone, Email, Password)"
////        )
//
//        // Setup onClickListeners for menu items
//        setupOnClickListeners(view)
//
//        return view
//    }
//
//    private fun loadUserInfo() {
//        val user = auth.currentUser
//        user?.let {
//            // Get user data from Firestore using UID
//            firestore.collection("admin").document(it.uid).get()
//                .addOnSuccessListener { document ->
//                    if (document.exists()) {
//                        // Load user details
//                        val name = document.getString("name") ?: "No name available"
//                        val email = it.email
//                            ?: "No email available" // Get email directly from the current user
//                        val phone = document.getString("phone") ?: "No phone available"
//
//                        // Log the fetched values for debugging
//                        Log.d(
//                            "AdminProfileFragment",
//                            "Fetched user data: Name: $name, Email: $email, Phone: $phone"
//                        )
//
//                        // Update the UI
//                        profileName.text = name
//                        profileEmail.text = email
//                        profilePhone.text = phone
//
//                        // Load user avatar using Glide
//                        val avatarUrl = document.getString("avatarUrl")
//                        if (!avatarUrl.isNullOrEmpty()) {
//                            Glide.with(this)
//                                .load(avatarUrl)
//                                .placeholder(R.drawable.baseline_person_24)
//                                .error(R.drawable.baseline_person_24)
//                                .into(profileImage)
//                        } else {
//                            profileImage.setImageResource(R.drawable.baseline_person_24)
//                        }
//                    } else {
//                        Log.d("AdminProfileFragment", "No such document")
//                        showDefaultUserData()
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.e("AdminProfileFragment", "Error getting user document", exception)
//                    showErrorLoadingData()
//                }
//        } ?: Log.w("AdminProfileFragment", "No current user")
//    }
//
//    private fun showDefaultUserData() {
//        profileName.text = "Admin"
//        profileEmail.text = "admin@gmail.com"
//        profilePhone.text = "081234567890"
//        profileImage.setImageResource(R.drawable.baseline_person_24)
//    }
//
//    private fun showErrorLoadingData() {
//        profileName.text = "Admin"
//        profileEmail.text = "admin@gmail.com"
//        profilePhone.text = "081234567890"
//        profileImage.setImageResource(R.drawable.baseline_person_24)
//    }
//
//    private fun setSpannableString(textView: TextView, title: String, subtitle: String) {
//        val fullText = "$title\n$subtitle"
//        val spannableString = SpannableString(fullText)
//
//        // Apply larger text size for title
//        spannableString.setSpan(
//            AbsoluteSizeSpan(16, true), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        // Apply smaller text size for subtitle
//        spannableString.setSpan(
//            AbsoluteSizeSpan(12, true),
//            title.length + 1,
//            spannableString.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//
//        textView.text = spannableString
//    }
//
////    private fun setupOnClickListeners(view: View) {
////        val myOrdersButton: TextView = view.findViewById(R.id.myOrders)
////        myOrdersButton.setOnClickListener {
////            val itemFragment = AdminItemFragment()
////            fragmentManager?.beginTransaction()?.apply {
////                replace(R.id.nav_host_fragment, itemFragment)
////                addToBackStack(null)
////                commit()
////            }
////        }
////
////        val myReviewsButton: TextView = view.findViewById(R.id.myReviews)
////        myReviewsButton.setOnClickListener {
////            val reviewFragment = AdminReviewFragment()
////            fragmentManager?.beginTransaction()?.apply {
////                replace(R.id.nav_host_fragment, reviewFragment)
////                addToBackStack(null)
////                commit()
////            }
////        }
////
////        val settingsButton: TextView = view.findViewById(R.id.settings)
////        settingsButton.setOnClickListener {
////            val settingFragment = AdminSettingFragment()
////            fragmentManager?.beginTransaction()?.apply {
////                replace(R.id.nav_host_fragment, settingFragment)
////                addToBackStack(null)
////                commit()
////            }
////        }
////    }
//    private fun setupOnClickListeners(view: View) {
//        // Navigate to AdminReviewFragment
//        val myReviewsButton: TextView = view.findViewById(R.id.myReviews)
//        myReviewsButton.setOnClickListener {
//            findNavController().navigate(R.id.action_adminProfileFragment_to_adminReviewFragment)
//        }
//    }
//
//    private fun logoutUser() {
//        auth.signOut()
//        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
//        startActivity(
//            Intent(
//                requireContext(),
//                LoginActivity::class.java
//            )
//        ) // Navigate to LoginActivity
//        activity?.finish() // End current activity
//    }
//}