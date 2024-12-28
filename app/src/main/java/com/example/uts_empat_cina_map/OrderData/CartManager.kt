package com.example.uts_empat_cina_map.OrderData

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun getCartItems(): List<CartItem> {
        return cartItems
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun removeCartItem(cartItem: CartItem) {
        cartItems.remove(cartItem)
    }

    fun addOrUpdateCartItem(foodItem: FoodItem, quantity: Int) {
        // Check if the item already exists in the cart
        val existingItem = cartItems.find { it.foodItem.id == foodItem.id }

        if (existingItem != null) {
            // If the item exists, increment the quantity
            existingItem.quantity += quantity
        } else {
            // If the item doesn't exist, add it to the cart
            cartItems.add(CartItem(foodItem, quantity))
        }
    }
}
