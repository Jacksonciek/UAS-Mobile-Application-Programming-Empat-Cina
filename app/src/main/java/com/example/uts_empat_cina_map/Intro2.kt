package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Intro2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro2)

        // Initialize views using findViewById
        //fix te error
        val btnPrevious = findViewById<ImageButton>(R.id.btn_previous)
        val btnNext = findViewById<ImageButton>(R.id.btn_next)
        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)

        // Navigate to the previous activity (StartActivity)
        btnPrevious.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish() // Optionally finish the current activity
        }

        // Navigate to the next activity (LoginActivity)
        btnNext.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Optionally finish the current activity
        }

        // You can update the page indicators dynamically if necessary
        updatePageIndicators(2)  // Assume this is page 2 in a sequence of 3
    }

    private fun updatePageIndicators(currentPage: Int) {
        // Initialize views
        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)

        // Reset all dots to inactive
        dot1.setBackgroundResource(R.drawable.indicator_inactive)
        dot2.setBackgroundResource(R.drawable.indicator_inactive)
        dot3.setBackgroundResource(R.drawable.indicator_inactive)

        // Set the current page dot to active
        when (currentPage) {
            1 -> dot1.setBackgroundResource(R.drawable.indicator_active)
            2 -> dot2.setBackgroundResource(R.drawable.indicator_active)
            3 -> dot3.setBackgroundResource(R.drawable.indicator_active)
        }
    }
}
