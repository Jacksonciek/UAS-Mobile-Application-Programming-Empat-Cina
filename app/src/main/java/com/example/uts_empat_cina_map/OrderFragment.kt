package com.example.uts_empat_cina_map.Order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.uts_empat_cina_map.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        // Set up ViewPager with the sections adapter.
        viewPager.adapter = SectionsPagerAdapter(requireActivity())

        // Attach TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All"
                1 -> "Main Course"
                2 -> "Drinks"
                3 -> "Desserts"
                4 -> "Snacks"
                5 -> "Appetizers"
                else -> null
            }
        }.attach()

        // Set the default tab to All (position 0)
        viewPager.currentItem = 0

        return view
    }

    private inner class SectionsPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 6 // Total number of tabs

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AllFoodFragment() // Fragment for All Food Items
                1 -> MainCourseFragment()
                2 -> DrinksFragment()
                3 -> DessertsFragment()
                4 -> SnacksFragment()
                5 -> AppetizersFragment()
                else -> AllFoodFragment()
            }
        }
    }
}
