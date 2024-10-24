package com.example.uts_empat_cina_map.OrderData

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodItem(
    val name: String,
    val imageResource: Int,
    val isFavorite: Boolean = false,
    val description: String,
    val price: Double,
    val imageUrl: String
) : Parcelable
