package com.example.uts_empat_cina_map.Order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.example.uts_empat_cina_map.OrderData.FoodItem
import com.example.uts_empat_cina_map.R

class FoodItemAdapter(
    private val context: Context,
    private var foodItemList: List<FoodItem>
) : RecyclerView.Adapter<FoodItemAdapter.FoodViewHolder>() {

    // Update data method
    fun updateData(newFoodItems: List<FoodItem>) {
        foodItemList = newFoodItems
        notifyDataSetChanged() // Refresh the adapter with new data
    }

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodPrice: TextView = itemView.findViewById(R.id.foodPrice)
        val stockQuantity: TextView = itemView.findViewById(R.id.stockQuantity) // New field for stock quantity
        val location: TextView = itemView.findViewById(R.id.location) // New field for location

        init {
            itemView.setOnClickListener {
                val foodItem = foodItemList[adapterPosition]
                val bundle = Bundle().apply {
                    putParcelable("food_item", foodItem)
                }
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FoodItemDetailFragment().apply { arguments = bundle })
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val foodItem = foodItemList[position]
        holder.foodName.text = foodItem.name
        holder.foodPrice.text = "Rp. ${foodItem.price}/pcs"
        holder.stockQuantity.text = "Stock : ${foodItem.stock} left" // Display stock quantity
        holder.location.text = foodItem.location // Display location
        Glide.with(context).load(foodItem.imageUrl).into(holder.foodImage) // Load image using Glide
    }

    override fun getItemCount(): Int {
        return foodItemList.size
    }
}
