package com.example.uts_empat_cina_map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlusItemFragment : Fragment() { // Extend Fragment

    private var currentNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and return the view
        val view = inflater.inflate(R.layout.fragment_plus_item, container, false)

        // Find views by their IDs
        val btnIncrement: Button = view.findViewById(R.id.btnIncrement)
        val btnDecrement: Button = view.findViewById(R.id.btnDecrement)
        val tvNumber: TextView = view.findViewById(R.id.tvNumber)

        // Set initial value from the TextView
        currentNumber = tvNumber.text.toString().toInt()

        // Set onClickListener for the Increment button
        btnIncrement.setOnClickListener {
            if (currentNumber < 99) { // Upper limit
                currentNumber++
                tvNumber.text = String.format("%02d", currentNumber)
            }
        }

        // Set onClickListener for the Decrement button
        btnDecrement.setOnClickListener {
            if (currentNumber > 0) { // Lower limit
                currentNumber--
                tvNumber.text = String.format("%02d", currentNumber)
            }
        }

        // Return the inflated view
        return view
    }
}
