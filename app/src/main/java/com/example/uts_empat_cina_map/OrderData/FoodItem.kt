package com.example.uts_empat_cina_map.OrderData

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodItem(
    var name: String = "", // Default values to ensure no-argument constructor works
    var location: String = "",
    var price: Double = 0.0,
    var description: String = "",
    var category: String = "",
    var stock: Int = 0,
    var imageUrl: String = "",
    var isFavorite: Boolean = false // If applicable
) : Parcelable {
    // No-argument constructor is provided by the default values
}
