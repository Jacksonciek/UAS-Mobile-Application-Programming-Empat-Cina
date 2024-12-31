package com.example.uts_empat_cina_map.OrderData

data class PaymentMethod(
    val paymentType: String,
    val labelName: String,
    val cardNumber: String,
    val description: String
)
