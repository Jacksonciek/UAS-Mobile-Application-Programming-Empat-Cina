package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Set click listener for the whole screen
        val startView: View = findViewById(R.id.startView)
        startView.setOnClickListener {
            // Move to LoginActivity when the screen is tapped
            val intent = Intent(this, Intro2 ::class.java)
            startActivity(intent)
            finish() // Close StartActivity so it's not on the back stack
        }
    }
}
