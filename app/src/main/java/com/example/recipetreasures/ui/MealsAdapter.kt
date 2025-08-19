package com.example.recipetreasures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipetreasures.data.db.AppDatabase
import com.example.recipetreasures.data.model.Meals
import com.example.recipetreasures.data.repo.FavoredMealRepository
import com.example.recipetreasures.databinding.ItemMealBinding
import com.example.recipetreasures.ui.FavoredMealViewModel
import com.example.recipetreasures.ui.FavoredMealViewModelFactory
import com.example.recipetreasures.ui.MealsUiModel
import kotlin.getValue

class MealsAdapter(
    private val meals: MutableList<MealsUiModel>,
    private val onItemClick: (MealsUiModel) -> Unit,
    private val onFavoriteClick: (MealsUiModel) -> Unit
) : RecyclerView.Adapter<MealsAdapter.MealViewHolder>() {

    inner class MealViewHolder(val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: MealsUiModel) {
            binding.titletv.text = meal.name
            binding.categorytv.text = meal.category
            binding.mealsAlttv.text = meal.strMealAlternate ?: ""

            Glide.with(binding.imageView)
                .load(meal.thumbnailUrl)
                .into(binding.imageView)

            binding.favoriteButton.setColorFilter(
                if (meal.isFavored)  ContextCompat.getColor(binding.root.context, android.R.color.holo_red_dark) else android.graphics.Color.GRAY,
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            binding.favoriteButton.setOnClickListener {
                onFavoriteClick(meal)
                meal.isFavored = !meal.isFavored

                binding.favoriteButton.setColorFilter(
                    if (meal.isFavored) ContextCompat.getColor(binding.root.context, android.R.color.holo_red_dark)
                    else android.graphics.Color.GRAY,
                    android.graphics.PorterDuff.Mode.SRC_IN
                )

                // Update the adapter so RecyclerView doesn't override state
                notifyItemChanged(adapterPosition)
            }

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

    fun updateData(newMeals: List<MealsUiModel>) {
        (meals as MutableList).clear()
        meals.addAll(newMeals)
        notifyDataSetChanged()
    }

}
