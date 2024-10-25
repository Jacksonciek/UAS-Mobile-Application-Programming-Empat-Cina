package com.example.uts_empat_cina_map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import com.bumptech.glide.Glide
import com.example.uts_empat_cina_map.Order.OrderFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    // Firebase instances
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profilePhone: TextView
    private lateinit var profileImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize views
        profileName = view.findViewById(R.id.profileName)
        profileEmail = view.findViewById(R.id.profileEmail)
        profilePhone = view.findViewById(R.id.profilePhone) // Make sure you have this TextView in your XML
        profileImage = view.findViewById(R.id.profileImage)

        // Load user info
        loadUserInfo()

        // Set SpannableString for My Orders
        setSpannableString(view.findViewById(R.id.myOrders), "My Orders", "Already have 10 orders")

        // Set SpannableString for Shipping Addresses
        setSpannableString(view.findViewById(R.id.shippingAddresses), "Shipping Addresses", "02 Addresses")

        // Set SpannableString for Payment Method
        setSpannableString(view.findViewById(R.id.paymentMethod), "Payment Method", "You have 2 cards")

        // Set SpannableString for My Reviews
        setSpannableString(view.findViewById(R.id.myReviews), "My Reviews", "Reviews for 5 items")

        // Set SpannableString for Settings
        setSpannableString(view.findViewById(R.id.settings), "Settings", "Notification, Password, FAQ, Contact")

        // Setup onClickListeners
        setupOnClickListeners(view)

        return view
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        user?.let {
            // Get user data from Firestore
            firestore.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Load user details
                        profileName.text = document.getString("name") ?: "No name available"
                        profileEmail.text = document.getString("email") ?: "No email available"
                        profilePhone.text = document.getString("phone") ?: "No phone available"

                        // Load user avatar using Glide
                        val avatarUrl = document.getString("avatarUrl")
                        if (!avatarUrl.isNullOrEmpty()) {
                            Glide.with(this) // Use 'this' to refer to the Fragment context
                                .load(avatarUrl)
                                .placeholder(R.drawable.baseline_person_24) // Default avatar while loading
                                .error(R.drawable.baseline_person_24) // Error placeholder
                                .into(profileImage)
                        } else {
                            profileImage.setImageResource(R.drawable.baseline_person_24) // Default avatar
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle the error
                }
        }
    }

    private fun setSpannableString(textView: TextView, title: String, subtitle: String) {
        val fullText = "$title\n$subtitle"
        val spannableString = SpannableString(fullText)

        // Apply larger text size for title
        spannableString.setSpan(
            AbsoluteSizeSpan(16, true), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Apply smaller text size for subtitle
        spannableString.setSpan(
            AbsoluteSizeSpan(12, true), title.length + 1, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        textView.text = spannableString
    }

    private fun setupOnClickListeners(view: View) {
        val navigateButton: TextView = view.findViewById(R.id.myOrders)
        navigateButton.setOnClickListener {
            val orderFragment = OrderFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, orderFragment)
                addToBackStack(null)
                commit()
            }
        }

        val shippingButton: TextView = view.findViewById(R.id.shippingAddresses)
        shippingButton.setOnClickListener {
            val shippingFragment = ShippingFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, shippingFragment)
                addToBackStack(null)
                commit()
            }
        }

        val paymentButton: TextView = view.findViewById(R.id.paymentMethod)
        paymentButton.setOnClickListener {
            val paymentFragment = PaymentFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, paymentFragment)
                addToBackStack(null)
                commit()
            }
        }

        val reviewButton: TextView = view.findViewById(R.id.myReviews)
        reviewButton.setOnClickListener {
            val reviewFragment = ReviewFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, reviewFragment)
                addToBackStack(null)
                commit()
            }
        }

        val settingButton: TextView = view.findViewById(R.id.settings)
        settingButton.setOnClickListener {
            val settingFragment = SettingFragment()
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, settingFragment)
                addToBackStack(null)
                commit()
            }
        }
    }
}
