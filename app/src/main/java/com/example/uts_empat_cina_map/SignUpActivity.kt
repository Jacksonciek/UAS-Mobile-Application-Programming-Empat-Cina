    package com.example.uts_empat_cina_map

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.EditText
    import android.widget.TextView
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.firestore.FirebaseFirestore

    class SignUpActivity : AppCompatActivity() {

        private lateinit var auth: FirebaseAuth
        private lateinit var firestore: FirebaseFirestore
        private lateinit var signupEmail: EditText
        private lateinit var signupPassword: EditText
        private lateinit var signupPhone: EditText
        private lateinit var signupFullName: EditText // Add reference for full name EditText
        private lateinit var signupButton: Button
        private lateinit var loginRedirectText: TextView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_sign_up)

            auth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()

            signupEmail = findViewById(R.id.signup_email)
            signupPassword = findViewById(R.id.signup_password)
            signupPhone = findViewById(R.id.signup_phone)
            signupFullName = findViewById(R.id.signup_fullname) // Initialize full name EditText
            signupButton = findViewById(R.id.signup_button)
            loginRedirectText = findViewById(R.id.loginRedirectText)
// Inside onCreate()
            val showPasswordButton: Button = findViewById(R.id.show_password_button)
            var isPasswordVisible = false

            showPasswordButton.setOnClickListener {
                if (isPasswordVisible) {
                    // Hide password
                    signupPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                    showPasswordButton.text = "Show"
                } else {
                    // Show password
                    signupPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    showPasswordButton.text = "Hide"
                }
                // Move cursor to the end
                signupPassword.setSelection(signupPassword.text.length)
                isPasswordVisible = !isPasswordVisible
            }

            signupButton.setOnClickListener {
                val user = signupEmail.text.toString().trim()
                val pass = signupPassword.text.toString().trim()
                val phone = signupPhone.text.toString().trim()
                val fullName = signupFullName.text.toString().trim() // Get full name

                if (user.isEmpty()) {
                    signupEmail.error = "Email cannot be empty"
                    return@setOnClickListener
                }
                if (pass.isEmpty()) {
                    signupPassword.error = "Password cannot be empty"
                    return@setOnClickListener
                }
                if (phone.isEmpty()) {
                    signupPhone.error = "Phone number cannot be empty"
                    return@setOnClickListener
                }
                if (fullName.isEmpty()) { // Validate full name
                    signupFullName.error = "Full name cannot be empty"
                    return@setOnClickListener
                }

                auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@SignUpActivity, "SignUp Successful", Toast.LENGTH_SHORT).show()
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        // Create a user document in Firestore with the actual full name
                        val userData = hashMapOf(
                            "name" to fullName, // Use the user's full name
                            "avatarUrl" to null,
                            "phone" to phone
                        )

                        firestore.collection("users").document(userId).set(userData)
                            .addOnSuccessListener {
                                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Failed to create user document: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this@SignUpActivity, "SignUp Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            loginRedirectText.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            }
        }
    }
