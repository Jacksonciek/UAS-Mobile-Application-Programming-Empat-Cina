package com.example.uts_empat_cina_map.OrderData

object CartManager {
    private val cartItems = mutableListOf<CartItem>() // CartItem should contain food details and quantity

    fun addItemToCart(foodItem: FoodItem, quantity: Int) {
        cartItems.add(CartItem(foodItem, quantity))
    }

    fun getCartItems(): List<CartItem> {
        return cartItems
    }

    fun clearCart() {
        cartItems.clear()
    }
}
