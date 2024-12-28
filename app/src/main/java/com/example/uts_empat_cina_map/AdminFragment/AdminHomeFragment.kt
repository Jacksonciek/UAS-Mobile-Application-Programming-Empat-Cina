// Update AdminHomeFragment.kt
package com.example.uts_empat_cina_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class AdminHomeFragment : Fragment() {

    private var isValueHidden = true
    private val actualValue = "Rp 500.367,34"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment (fragment_admin_home.xml)
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incomeValueTextView: TextView = view.findViewById(R.id.incomeValue)
        val toggleButton: Button = view.findViewById(R.id.toggleButton)

        toggleButton.setOnClickListener {
            if (isValueHidden) {
                incomeValueTextView.text = actualValue
                toggleButton.setBackgroundResource(R.drawable.eye_open)
            } else {
                incomeValueTextView.text = "*************"
                toggleButton.setBackgroundResource(R.drawable.eye_closed)
            }
            isValueHidden = !isValueHidden
        }
    }
}