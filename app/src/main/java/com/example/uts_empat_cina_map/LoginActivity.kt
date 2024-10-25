package com.example.uts_empat_cina_map

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.developer.gbuttons.GoogleSignInButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var signupRedirectText: TextView
    private lateinit var loginButton: Button
    private lateinit var redirectAdmin: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var forgotPassword: TextView
    private lateinit var googleBtn: GoogleSignInButton
    private lateinit var gOptions: GoogleSignInOptions
    private lateinit var gClient: GoogleSignInClient
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Views
        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById(R.id.signUpRedirectText)
        forgotPassword = findViewById(R.id.forgot_password)
        googleBtn = findViewById(R.id.googleBtn)
        redirectAdmin = findViewById(R.id.adminRedirectText)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = loginEmail.text.toString()
            val pass = loginPassword.text.toString()

            if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (!pass.isEmpty()) {
                    auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener {
                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                            // Check if the logged-in user is the admin
                            if (email == "admin@gmail.com") {
                                startActivity(Intent(this@LoginActivity, AdminActivity::class.java)) // Redirect to Admin Activity
                            } else {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java)) // Redirect to User Activity
                            }
                            finish() // Close the login activity
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    loginPassword.error = "Empty fields are not allowed"
                }
            } else if (email.isEmpty()) {
                loginEmail.error = "Empty fields are not allowed"
            } else {
                loginEmail.error = "Please enter correct email"
            }
        }

        signupRedirectText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
        }

        redirectAdmin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, admin::class.java))
        }

        forgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this@LoginActivity)
            val dialogView = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val emailBox = dialogView.findViewById<EditText>(R.id.emailBox)

            builder.setView(dialogView)
            val dialog = builder.create()

            dialogView.findViewById<Button>(R.id.btnReset).setOnClickListener {
                val userEmail = emailBox.text.toString()

                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(this@LoginActivity, "Enter your registered email id", Toast.LENGTH_SHORT).show()
                } else {
                    auth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@LoginActivity, "Check your email", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            } else {
                                Toast.makeText(this@LoginActivity, "Unable to send, failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.show()
        }

        // Google SignIn Options
        gOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gClient = GoogleSignIn.getClient(this, gOptions)

        val gAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (gAccount != null) {
            finish()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // Google SignIn Result Launcher
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        task.getResult(ApiException::class.java)
                        finish()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } catch (e: ApiException) {
                        Toast.makeText(this@LoginActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        googleBtn.setOnClickListener {
            val signInIntent = gClient.signInIntent
            activityResultLauncher.launch(signInIntent)
        }
    }
}
