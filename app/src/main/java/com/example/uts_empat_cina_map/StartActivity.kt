package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener

class StartActivity : AppCompatActivity() {
    private lateinit var userName: TextView
    private lateinit var logout: Button
    private lateinit var gClient: GoogleSignInClient
    private lateinit var gOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Initialize Views
        logout = findViewById(R.id.logout)
        userName = findViewById(R.id.userName)

        // Google Sign-In Options
        gOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gClient = GoogleSignIn.getClient(this, gOptions)

        // Check last signed-in Google account
        val gAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        if (gAccount != null) {
            val gName = gAccount.displayName
//            userName.text = gName
        }

        // Logout Button Listener
        logout.setOnClickListener {
            gClient.signOut().addOnCompleteListener { _ ->
                finish()
                startActivity(Intent(this@StartActivity, LoginActivity::class.java))
            }
        }

        // Login via Biometric Listener
        val navigateButton: Button = findViewById(R.id.loginBiometric)
        navigateButton.setOnClickListener {
            val intent = Intent(this, biometric::class.java)
            startActivity(intent)
        }

    }
}
