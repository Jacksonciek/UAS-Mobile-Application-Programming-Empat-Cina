package com.example.uts_empat_cina_map.OrderData

object CartManager {
    private val cartItems = mutableListOf<CartItem>() // CartItem should contain food details and quantity

    fun addItemToCart(foodItem: FoodItem, quantity: Int) {
        cartItems.add(CartItem(foodItem, quantity))
    }

    fun getCartItems(): MutableList<CartItem> { // Return MutableList instead of List
        return cartItems
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun removeCartItem(cartItem: CartItem) {
        // Remove the item from the cart
        cartItems.remove(cartItem)
    }

    fun addOrUpdateCartItem(cartItem: CartItem) {
        // Check if the item already exists in the cart
        val existingItem = cartItems.find { it.foodItem.name == cartItem.foodItem.name }

        if (existingItem != null) {
            // If item exists, increment its quantity
            existingItem.quantity += cartItem.quantity
        } else {
            // If item doesn't exist, add a new item
            cartItems.add(cartItem)
        }
    }

}
