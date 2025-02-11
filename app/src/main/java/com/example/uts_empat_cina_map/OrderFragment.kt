package com.example.uts_empat_cina_map.Order

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.uts_empat_cina_map.CheckoutFragment
import com.example.uts_empat_cina_map.R
import com.example.uts_empat_cina_map.notification
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var searchEditText: EditText

    private val fragmentList = listOf(
        AllFoodFragment(),
        MainCourseFragment(),
        DrinksFragment(),
        DessertsFragment(),
        SnacksFragment(),
        AppetizersFragment()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        searchEditText = view.findViewById(R.id.searchEditText) // Assuming your EditText ID is searchEditText
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        // Set up ViewPager with sections adapter.
        viewPager.adapter = SectionsPagerAdapter(requireActivity(), fragmentList)

        // Attach TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Set teks untuk tab berdasarkan posisi
            val tabTitles = listOf("All", "Main Course", "Drinks", "Desserts", "Snacks", "Appetizers")
            val customTabView = LayoutInflater.from(context).inflate(R.layout.tab_item, null)
            val tabTextView = customTabView.findViewById<TextView>(R.id.tabTextView)
            tabTextView.text = tabTitles[position]

            // Terapkan custom view ke tab
            tab.customView = customTabView
        }.attach()


        val cartButton: Button = view.findViewById(R.id.cart_button)

        val notificationButton: Button = view.findViewById(R.id.mail_button)

        cartButton.setOnClickListener {
            val fragment = CheckoutFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        notificationButton.setOnClickListener {
            val fragment = notification()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        // Add a listener to the EditText to filter data on text change
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                updateFragmentsSearch(query)  // Trigger search in all fragments
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return view
    }

    private fun updateFragmentsSearch(query: String) {
        fragmentList.forEach { fragment ->
            if (fragment is SearchableFragment) {
                fragment.filterData(query)  // Call filterData for each fragment
            }
        }
    }

    private inner class SectionsPagerAdapter(fa: FragmentActivity, private val fragments: List<Fragment>) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}
