package com.example.uts_empat_cina_map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.uts_empat_cina_map.Order.AllFoodFragment
import com.example.uts_empat_cina_map.Order.DrinksFragment
import com.example.uts_empat_cina_map.Order.FavoritesFragment
import com.example.uts_empat_cina_map.Order.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order, container, false)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)

        // Set up custom tabs after linking with ViewPager
        setupCustomTabs()

        return view
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(AllFoodFragment(), "All")
        adapter.addFragment(FavoritesFragment(), "Favorites")
        adapter.addFragment(DrinksFragment(), "Drinks")
        viewPager.adapter = adapter
    }

    private fun setupCustomTabs() {
        // First tab (All)
        val allTab = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        allTab.findViewById<TextView>(R.id.tab_title).text = "All"
        tabLayout.getTabAt(0)?.customView = allTab

        // Second tab (Favorites)
        val favoritesTab = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        favoritesTab.findViewById<TextView>(R.id.tab_title).text = "Favorites"
        tabLayout.getTabAt(1)?.customView = favoritesTab

        // Third tab (Drinks)
        val drinksTab = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        drinksTab.findViewById<TextView>(R.id.tab_title).text = "Drinks"
        tabLayout.getTabAt(2)?.customView = drinksTab
    }
}

