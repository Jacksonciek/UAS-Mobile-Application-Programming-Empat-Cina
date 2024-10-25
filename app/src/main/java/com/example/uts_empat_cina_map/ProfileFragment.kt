package com.example.uts_empat_cina_map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import com.example.uts_empat_cina_map.Order.OrderFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    // Firebase instances
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Set SpannableString untuk My Orders
        setSpannableString(view.findViewById(R.id.myOrders), "My Orders", "Already have 10 orders")

        // Set SpannableString untuk Shipping Addresses
        setSpannableString(view.findViewById(R.id.shippingAddresses), "Shipping Addresses", "02 Addresses")

        // Set SpannableString untuk Payment Method
        setSpannableString(view.findViewById(R.id.paymentMethod), "Payment Method", "You have 2 cards")

        // Set SpannableString untuk My Reviews
        setSpannableString(view.findViewById(R.id.myReviews), "My Reviews", "Reviews for 5 items")

        // Set SpannableString untuk Settings
        setSpannableString(view.findViewById(R.id.settings), "Settings", "Notification, Password, FAQ, Contact")

        // Setup onClickListeners
        setupOnClickListeners(view)

        return view
    }

    private fun setSpannableString(textView: TextView, title: String, subtitle: String) {
        val fullText = "$title\n$subtitle"
        val spannableString = SpannableString(fullText)

        // Terapkan ukuran teks besar untuk title
        spannableString.setSpan(
            AbsoluteSizeSpan(16, true), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Terapkan ukuran teks kecil untuk subtitle
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
