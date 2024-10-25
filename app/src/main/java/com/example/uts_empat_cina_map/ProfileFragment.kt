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
import android.util.Log
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

        val buttonnotification: ImageView = view.findViewById(R.id.iconLeft)

        buttonnotification.setOnClickListener {
            val fragment = notification()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Initialize views
        profileName = view.findViewById(R.id.profileName)
        profileEmail = view.findViewById(R.id.profileEmail)
        profilePhone = view.findViewById(R.id.profilePhone)
        profileImage = view.findViewById(R.id.profileImage)

        // Load user info
        loadUserInfo()

        // Set SpannableString for various menus
        setSpannableString(view.findViewById(R.id.myOrders), "My Orders", "Already have 10 orders")
        setSpannableString(view.findViewById(R.id.shippingAddresses), "Shipping Addresses", "02 Addresses")
        setSpannableString(view.findViewById(R.id.paymentMethod), "Payment Method", "You have 2 cards")
        setSpannableString(view.findViewById(R.id.myReviews), "My Reviews", "Reviews for 5 items")
        setSpannableString(view.findViewById(R.id.settings), "Settings", "Notification, Password, FAQ, Contact")

        // Setup onClickListeners for menu items
        setupOnClickListeners(view)

        return view
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        user?.let {
            // Get user data from Firestore using UID
            firestore.collection("users").document(it.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Load user details
                        val name = document.getString("name") ?: "No name available"
                        val email = it.email ?: "No email available" // Get email directly from the current user
                        val phone = document.getString("phone") ?: "No phone available"

                        // Log the fetched values for debugging
                        Log.d("ProfileFragment", "Fetched user data: Name: $name, Email: $email, Phone: $phone")

                        // Update the UI
                        profileName.text = name
                        profileEmail.text = email
                        profilePhone.text = phone

                        // Load user avatar using Glide
                        val avatarUrl = document.getString("avatarUrl")
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
                        Log.d("ProfileFragment", "No such document")
                        showDefaultUserData()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("ProfileFragment", "Error getting user document", exception)
                    showErrorLoadingData()
                }
        } ?: Log.w("ProfileFragment", "No current user")
    }

    private fun showDefaultUserData() {
        profileName.text = "No name available"
        profileEmail.text = "No email available"
        profilePhone.text = "No phone available"
        profileImage.setImageResource(R.drawable.baseline_person_24)
    }

    private fun showErrorLoadingData() {
        profileName.text = "Error loading data"
        profileEmail.text = "Error loading data"
        profilePhone.text = "Error loading data"
        profileImage.setImageResource(R.drawable.baseline_person_24)
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
