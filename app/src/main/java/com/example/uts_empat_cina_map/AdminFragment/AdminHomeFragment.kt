// Update AdminHomeFragment.kt
package com.example.uts_empat_cina_map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.firestore.FirebaseFirestore

class AdminHomeFragment : Fragment() {

    private var isValueHidden = true
    private val adminViewModel: AdminViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment (fragment_admin_home.xml)
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val incomeValueTextView: TextView = view.findViewById(R.id.incomeValue)
        val toggleButton: Button = view.findViewById(R.id.toggleButton)
        val pieChart: PieChart = view.findViewById(R.id.pieChart)

        adminViewModel.totalProfit.observe(viewLifecycleOwner) { totalProfit ->
            if (!isValueHidden) {
                incomeValueTextView.text = "Rp. $totalProfit"
            }
        }

        toggleButton.setOnClickListener {
            if (isValueHidden) {
                adminViewModel.totalProfit.value?.let { totalProfit ->
                    incomeValueTextView.text = "Rp. $totalProfit"
                }
                toggleButton.setBackgroundResource(R.drawable.eye_open)
            } else {
                incomeValueTextView.text = "*************"
                toggleButton.setBackgroundResource(R.drawable.eye_closed)
            }
            isValueHidden = !isValueHidden
        }

        // Fetch category data from Firebase and update PieChart
        fetchCategoryData { categoryCounts ->
            updatePieChart(pieChart, categoryCounts)
        }
    }

    private fun fetchCategoryData(onResult: (Map<String, Int>) -> Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val categories = listOf("Appetizers", "Main Course", "Drinks", "Desserts", "Snacks")
        val categoryCounts = mutableMapOf<String, Int>()

        categories.forEach { category ->
            firestore.collection("items")
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener { documents ->
                    categoryCounts[category] = documents.size()
                    if (categoryCounts.size == categories.size) {
                        onResult(categoryCounts) // Callback with final data
                    }
                }
                .addOnFailureListener {
                    categoryCounts[category] = 0 // Handle failure
                    if (categoryCounts.size == categories.size) {
                        onResult(categoryCounts) // Callback even on failure
                    }
                }
        }
    }

    private fun updatePieChart(pieChart: PieChart, categoryCounts: Map<String, Int>) {
        val pieEntries = categoryCounts.map { PieEntry(it.value.toFloat(), it.key) }

        val dataSet = PieDataSet(pieEntries, null)
        dataSet.colors = listOf(
            Color.rgb(244, 67, 54),  // Red
            Color.rgb(33, 150, 243), // Blue
            Color.rgb(76, 175, 80),  // Green
            Color.rgb(255, 235, 59), // Yellow
            Color.rgb(156, 39, 176)  // Purple
        )
        dataSet.sliceSpace = 2f
        dataSet.selectionShift = 8f
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTypeface = android.graphics.Typeface.DEFAULT_BOLD // Make text bold

        val data = PieData(dataSet)
        data.setValueTextSize(14f)
        data.setValueTextColor(Color.BLACK)

        pieChart.data = data
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 40f
        pieChart.transparentCircleRadius = 45f
        pieChart.setDrawEntryLabels(true)
        pieChart.setEntryLabelTextSize(14f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTypeface(android.graphics.Typeface.DEFAULT_BOLD) // Make label text bold
        pieChart.centerText = "Food Distribution"
        pieChart.setCenterTextSize(18f)
        pieChart.setCenterTextColor(Color.BLACK)
        pieChart.setCenterTextTypeface(android.graphics.Typeface.DEFAULT_BOLD)

        // Disable spinning for PieChart
        pieChart.isRotationEnabled = false

        val legend = pieChart.legend
        legend.isEnabled = false // Disable the legend to remove the color box

        pieChart.invalidate() // Refresh the chart
    }
}
