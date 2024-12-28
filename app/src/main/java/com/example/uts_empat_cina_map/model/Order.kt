package com.example.uts_empat_cina_map.model

import com.google.firebase.firestore.DocumentId

data class Order(
    @DocumentId val id: String? = null,  // Firestore document ID (optional)
    val userId: String,
    val totalPrice: Double,              // Total price of the order
    val totalQuantity: Int,              // Total quantity of items
    val items: List<OrderItem>,          // List of items in the order
    val paymentMethod: String,           // Payment method chosen by the user
    val timestamp: Long,                 // Timestamp when the order was placed
    var orderStatus: String = "On Process"
) {
    // Empty constructor for Firebase to automatically convert documents to Order objects
    constructor() : this("", "", 0.0, 0, emptyList(), "", 0, "on process")
}

data class OrderItem(
    val name: String = "",           // Name of the food item, default empty string
    val price: Double = 0.0,         // Price of the food item, default 0.0
    val quantity: Int = 0            // Quantity of the food item, default 0
) {
    // Firebase needs a no-argument constructor, which is automatically provided with default values
    constructor() : this("", 0.0, 0)
}
