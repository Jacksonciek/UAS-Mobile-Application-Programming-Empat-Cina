// Create AdminViewModel.kt
package com.example.uts_empat_cina_map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdminViewModel : ViewModel() {
    private val _totalProfit = MutableLiveData<Double>()
    val totalProfit: LiveData<Double> get() = _totalProfit

    fun setTotalProfit(profit: Double) {
        _totalProfit.value = profit
    }
}