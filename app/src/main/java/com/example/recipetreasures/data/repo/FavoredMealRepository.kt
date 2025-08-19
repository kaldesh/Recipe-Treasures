package com.example.recipetreasures.data.repo

import androidx.lifecycle.LiveData
import com.example.recipetreasures.data.db.FavoredMealDao
import com.example.recipetreasures.data.model.FavoredMeal

class FavoredMealRepository(private val dao: FavoredMealDao) {

    suspend fun getFavoredMeals(userEmail: String): List<FavoredMeal> =
        dao.getFavoredMeals(userEmail)

    suspend fun isMealFavored(mealId: String, userEmail: String): Boolean =
        dao.isMealFavored(mealId, userEmail) != null

    suspend fun addMealToFavorites(mealId: String, userEmail: String) =
        dao.addMealToFavorites(mealId, userEmail)

    suspend fun removeMealFromFavorites(mealId: String, userEmail: String) =
        dao.removeMealFromFavorites(mealId, userEmail)
}