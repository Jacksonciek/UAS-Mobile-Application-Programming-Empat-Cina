package com.example.uts_empat_cina_map.OrderData

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addOrUpdateCartItem(foodItem: FoodItem, quantity: Int) {
        // Check if an item with the same name already exists
        val existingItem = cartItems.find { it.foodItem.name == foodItem.name }

        if (existingItem != null) {
            // Update the quantity of the existing item
            existingItem.quantity += quantity
        } else {
            // Add a new item to the cart
            cartItems.add(CartItem(foodItem, quantity))
        }
    }

    fun getCartItems(): List<CartItem> {
        return cartItems
    }

    fun removeCartItem(cartItem: CartItem) {
        cartItems.remove(cartItem)
    }

    fun clearCart() {
        cartItems.clear()
    }
}
