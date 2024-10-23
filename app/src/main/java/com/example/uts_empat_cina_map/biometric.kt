package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat

class biometric : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_biometric)

//        val biometricLoginButton: Button = findViewById(R.id.cobaBio)

        // Set up biometric prompt
        var biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(this),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!",
                        Toast.LENGTH_SHORT
                    ).show()

                    //Navigate to a new Activity
                    val intent = Intent(this@biometric, homepage::class.java)
                    startActivity(intent)
                    // Proceed to the next step, e.g., opening a new activity
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )

        // Create the prompt info
        var promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        // Set button click listener
//        biometricLoginButton.setOnClickListener {
//            val biometricManager = BiometricManager.from(this)
//            when (biometricManager.canAuthenticate()) {
//                BiometricManager.BIOMETRIC_SUCCESS -> {
//                    biometricPrompt.authenticate(promptInfo)
//                }
//                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
//                    Toast.makeText(this, "No biometric hardware available", Toast.LENGTH_SHORT).show()
//                }
//                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
//                    Toast.makeText(this, "Biometric hardware is currently unavailable", Toast.LENGTH_SHORT).show()
//                }
//                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
//                    Toast.makeText(this, "No biometric credentials enrolled", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        // Instant Biometric
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "No biometric hardware available", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Biometric hardware is currently unavailable", Toast.LENGTH_SHORT).show()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "No biometric credentials enrolled", Toast.LENGTH_SHORT).show()
            }
        }

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}