package com.example.recipetreasures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipetreasures.data.Meals
import com.example.recipetreasures.databinding.ItemMealBinding

class MealsAdapter(
    private val meals: MutableList<Meals>,
    private val onItemClick: (Meals) -> Unit
) : RecyclerView.Adapter<MealsAdapter.MealViewHolder>() {

    inner class MealViewHolder(val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: Meals) {
            binding.titletv.text = meal.strMeal ?: "No Name"
            binding.categorytv.text = meal.strCategory ?: "No Category"
            binding.mealsAlttv.text = meal.strMealAlternate ?: ""

            Glide.with(binding.imageView)
                .load(meal.strMealThumb)
                .into(binding.imageView)

            binding.root.setOnClickListener { onItemClick(meal) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount(): Int = meals.size

    fun updateData(newMeals: List<Meals>) {
        (meals as MutableList).clear()
        meals.addAll(newMeals)
        notifyDataSetChanged()
    }

}
