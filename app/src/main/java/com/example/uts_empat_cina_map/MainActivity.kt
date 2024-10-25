package com.example.uts_empat_cina_map

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.uts_empat_cina_map.adapter.OnBoardingViewPagerAdapter
import com.example.uts_empat_cina_map.model.OnBoardingData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter? = null
    private var tabLayout: TabLayout? = null
    private var onBoardingViewPager: ViewPager? = null
    private var getStarted: TextView? = null
    private var position = 0
    private var next: Button? = null
    private var prev: Button? = null
    private var mainLayout: RelativeLayout? = null
//    private lateinit var gClient: GoogleSignInClient
//    private lateinit var gOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize views
        tabLayout = findViewById(R.id.tab_indicator)
        next = findViewById(R.id.next)
        prev = findViewById(R.id.prev)
        getStarted = findViewById(R.id.get_started)
        mainLayout = findViewById(R.id.mainLayout)
        // Google Sign-In Options
//        gOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
//        gClient = GoogleSignIn.getClient(this, gOptions)
//
//        // Check last signed-in Google account
//        val gAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
//        if (gAccount != null) {
//            val gName = gAccount.displayName
////            userName.text = gName
//        }
//
//        // Login via Biometric Listener
//        val navigateButton: Button = findViewById(R.id.loginBiometric)
//        navigateButton.setOnClickListener {
//            val intent = Intent(this, biometric::class.java)
//            startActivity(intent)
//        }

        prev?.visibility = View.GONE // Hide prev button on the first page
        next?.visibility = View.VISIBLE // Show next button
        getStarted?.visibility = View.GONE // Initially hidden

        val onBoardingData: MutableList<OnBoardingData> = ArrayList()
        onBoardingData.add(OnBoardingData("Page 1", "Description 1", R.drawable.intro_satu))
        onBoardingData.add(OnBoardingData("Page 2", "Description 2", R.drawable.intro_dua))
        onBoardingData.add(OnBoardingData("Page 3", "Description 3", R.drawable.blank))

        // Set up the ViewPager with the OnBoardingViewPagerAdapter
        setOnBoardingViewPagerAdapter(onBoardingData)

        next?.setOnClickListener {
            position = onBoardingViewPager!!.currentItem
            if (position < onBoardingData.size - 1) {
                position++
                onBoardingViewPager!!.currentItem = position
                updateButtonVisibility()
            }
        }

        prev?.setOnClickListener {
            position = onBoardingViewPager!!.currentItem
            if (position > 0) {
                position--
                onBoardingViewPager!!.currentItem = position
                updateButtonVisibility()
            }
        }

        // Set up page change listener to change background dynamically
        onBoardingViewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                updateBackground(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        // Tab change listener to show/hide buttons
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                updateButtonVisibility()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        getStarted?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set the initial background for the first start
        updateBackground(0)
    }

    // Update the background dynamically based on the current page
    private fun updateBackground(position: Int) {
        when (position) {
            0 -> {
                mainLayout?.setBackgroundResource(R.drawable.intro_satu)
                mainLayout?.post {
                    mainLayout?.invalidate() // Force a refresh to avoid whitespace issue
                }
            }
            1 -> mainLayout?.setBackgroundResource(R.drawable.intro_dua)
            2 -> mainLayout?.setBackgroundResource(R.drawable.intro_tiga)
        }
    }

    // Update button visibility based on the current page
    private fun updateButtonVisibility() {
        when (position) {
            0 -> {
                prev?.visibility = View.GONE // Hide prev button on the first page
                next?.visibility = View.VISIBLE
                getStarted?.visibility = View.GONE
            }
            onBoardingViewPagerAdapter!!.count - 1 -> {
                next?.visibility = View.GONE
                prev?.visibility = View.VISIBLE
                getStarted?.visibility = View.VISIBLE
            }
            else -> {
                prev?.visibility = View.VISIBLE
                next?.visibility = View.VISIBLE
                getStarted?.visibility = View.GONE
            }
        }
    }

    // Set up the ViewPager with the adapter
    private fun setOnBoardingViewPagerAdapter(onBoardingData: List<OnBoardingData>) {
        onBoardingViewPager = findViewById(R.id.screenPager)
        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(this, onBoardingData)
        onBoardingViewPager!!.adapter = onBoardingViewPagerAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPager)
    }
}

//class MainActivity : AppCompatActivity() {
//
////    private lateinit var userName: TextView
////    private lateinit var logout: Button
//    private lateinit var gClient: GoogleSignInClient
//    private lateinit var gOptions: GoogleSignInOptions
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // Initialize Views
////        logout = findViewById(R.id.logout)
////        userName = findViewById(R.id.userName)
//
//        // Google Sign-In Options
//        gOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
//        gClient = GoogleSignIn.getClient(this, gOptions)
//
//        // Check last signed-in Google account
//        val gAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
//        if (gAccount != null) {
//            val gName = gAccount.displayName
////            userName.text = gName
//        }
//
//        // Logout Button Listener
////        logout.setOnClickListener {
////            gClient.signOut().addOnCompleteListener { _ ->
////                finish()
////                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
////            }
////        }
//
//        // Login via Biometric Listener
//        val navigateButton: Button = findViewById(R.id.loginBiometric)
//        navigateButton.setOnClickListener {
//            val intent = Intent(this, biometric::class.java)
//            startActivity(intent)
//        }
//    }
//}