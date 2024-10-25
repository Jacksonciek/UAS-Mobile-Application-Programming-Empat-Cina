package com.example.uts_empat_cina_map

import HomeFragment
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uts_empat_cina_map.Order.OrderFragment
import com.example.uts_empat_cina_map.PlusItemFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.api.ResourceDescriptor.History

class homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
//        val frameLayout: FrameLayout = findViewById(R.id.fragment_container)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
                    true
                }
                R.id.nav_order -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, OrderFragment()).commit()
                    true
                }
                R.id.nav_plus -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PlusItemFragment()).commit()
                    true
                }
                R.id.nav_history -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HistoryFragment()).commit()
                    true
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ProfileFragment()).commit()
                    true
                }
                else -> false
            }
        }


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }
}