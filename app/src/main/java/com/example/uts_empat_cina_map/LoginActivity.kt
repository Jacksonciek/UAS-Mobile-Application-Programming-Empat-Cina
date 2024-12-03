package com.example.uts_empat_cina_map

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
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
import jp.wasabeef.blurry.Blurry

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var signupRedirectText: TextView
    private lateinit var loginButton: Button
    private lateinit var forgotPassword: TextView
    private lateinit var googleBtn: GoogleSignInButton
    private lateinit var auth: FirebaseAuth
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

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener { loginUser() }
        signupRedirectText.setOnClickListener { navigateToSignUp() }
        forgotPassword.setOnClickListener { showForgotPasswordDialog() }

        // Initialize Google Sign-In
        initGoogleSignIn()

        // Check if user is already signed in
        checkAlreadySignedIn()
    }

    private fun loginUser() {
        val email = loginEmail.text.toString().trim()
        val pass = loginPassword.text.toString().trim()

        if (email.isEmpty()) {
            loginEmail.error = "Email cannot be empty"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.error = "Please enter a valid email"
            return
        }
        if (pass.isEmpty()) {
            loginPassword.error = "Password cannot be empty"
            return
        }

        // Sign in with email and password
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                navigateToNextActivity(email)
                finish() // Close the login activity
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Login Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToNextActivity(email: String) {
        // Redirect based on user role
        if (email == "admin@gmail.com") {
            startActivity(Intent(this, AdminActivity::class.java))
        } else {
            startActivity(Intent(this, homepage::class.java))
        }
    }

    private fun navigateToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun showForgotPasswordDialog() {
        // Blur the background
        Blurry.with(this)
            .radius(15)
            .sampling(2)
            .onto(findViewById(R.id.root_layout))

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_forgot, null)
        val emailBox = dialogView.findViewById<EditText>(R.id.emailBox)

        builder.setView(dialogView)
        val dialog = builder.create()

        dialogView.findViewById<Button>(R.id.btnReset).setOnClickListener {
            val userEmail = emailBox.text.toString().trim()
            if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(this, "Enter your registered email id", Toast.LENGTH_SHORT).show()
            } else {
                sendPasswordResetEmail(userEmail, dialog)
            }
        }

        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
            Blurry.delete(findViewById(R.id.root_layout))
        }

        // Set a transparent background for the dialog
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setOnDismissListener { Blurry.delete(findViewById(R.id.root_layout)) }

        dialog.show()
    }

    private fun sendPasswordResetEmail(userEmail: String, dialog: AlertDialog) {
        auth.sendPasswordResetEmail(userEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    Blurry.delete(findViewById(R.id.root_layout))
                } else {
                    Toast.makeText(this, "Unable to send reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun initGoogleSignIn() {
        gOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gClient = GoogleSignIn.getClient(this, gOptions)

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        task.getResult(ApiException::class.java)
                        // Proceed to the next activity
                        finish()
                        startActivity(Intent(this, biometric::class.java))
                    } catch (e: ApiException) {
                        Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        googleBtn.setOnClickListener {
            val signInIntent = gClient.signInIntent
            activityResultLauncher.launch(signInIntent)
        }
    }

    private fun checkAlreadySignedIn() {
        val gAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (gAccount != null) {
            finish()
            startActivity(Intent(this, biometric::class.java))
        }
    }
}
