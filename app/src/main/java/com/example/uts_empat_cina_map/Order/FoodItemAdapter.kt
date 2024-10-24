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
import com.example.uts_empat_cina_map.OrderData.FoodItem
import com.example.uts_empat_cina_map.R

class FoodItemAdapter(private val context: Context, private val foodItemList: List<FoodItem>) :
    RecyclerView.Adapter<FoodItemAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        val foodName: TextView = itemView.findViewById(R.id.foodName)

        init {
            itemView.setOnClickListener {
                // Get the current food item
                val foodItem = foodItemList[adapterPosition]

                // Create a bundle to pass food item data
                val bundle = Bundle().apply {
                    putParcelable("food_item", foodItem)
                }

                // Navigate to FoodItemDetailFragment
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
        holder.foodImage.setImageResource(foodItem.imageResource)
    }

    override fun getItemCount(): Int = foodItemList.size
}
