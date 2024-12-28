package com.example.uts_empat_cina_map

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class AdminHomeFragment : Fragment() {

    private var isValueHidden = true
    private val actualValue = "Rp 500.367,34"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment (fragment_admin_home.xml)
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up TextView and ToggleButton for income visibility
        val incomeValueTextView: TextView = view.findViewById(R.id.incomeValue)
        val toggleButton: Button = view.findViewById(R.id.toggleButton)
        val pieChart: PieChart = view.findViewById(R.id.pieChart) // Ensure correct ID

        toggleButton.setOnClickListener {
            if (isValueHidden) {
                incomeValueTextView.text = actualValue
                toggleButton.setBackgroundResource(R.drawable.eye_open)
            } else {
                incomeValueTextView.text = "*************"
                toggleButton.setBackgroundResource(R.drawable.eye_closed)
            }
            isValueHidden = !isValueHidden
        }

        // Add dummy data for PieChart
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(45f, "Marketing"))
        entries.add(PieEntry(25f, "Development"))
        entries.add(PieEntry(15f, "Sales"))
        entries.add(PieEntry(10f, "Operations"))
        entries.add(PieEntry(5f, "Support"))

        // Configure dataset
        val dataSet = PieDataSet(entries, "Income Distribution")
        dataSet.colors = listOf(
            Color.rgb(244, 67, 54),  // Red
            Color.rgb(33, 150, 243), // Blue
            Color.rgb(76, 175, 80),  // Green
            Color.rgb(255, 235, 59), // Yellow
            Color.rgb(156, 39, 176)  // Purple
        )
        dataSet.sliceSpace = 3f // Space between slices
        dataSet.selectionShift = 10f // Animation when slice is selected
        dataSet.valueTextSize = 12f // Text size for values
        dataSet.valueTextColor = Color.BLACK // Color for value text

        // Create PieData and set it to the PieChart
        val data = PieData(dataSet)
        data.setValueTextSize(14f)
        data.setValueTextColor(Color.DKGRAY)

        // Configure PieChart
        pieChart.data = data
        pieChart.setUsePercentValues(true) // Show percentages
        pieChart.description.isEnabled = false // Disable description text
        pieChart.isDrawHoleEnabled = true // Enable hole in the center
        pieChart.holeRadius = 40f // Radius of the center hole
        pieChart.transparentCircleRadius = 45f // Radius of transparent circle
        pieChart.setDrawEntryLabels(true) // Show labels for slices
        pieChart.setEntryLabelTextSize(12f) // Text size for entry labels
        pieChart.setEntryLabelColor(Color.BLACK) // Color for entry labels
        pieChart.centerText = "Income" // Center text
        pieChart.setCenterTextSize(16f) // Text size for center text
        pieChart.invalidate() // Refresh the chart
    }
}
