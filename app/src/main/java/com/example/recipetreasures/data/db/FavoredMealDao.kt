package com.example.recipetreasures.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.recipetreasures.data.model.FavoredMeal

@Dao
interface FavoredMealDao {
    @Query("SELECT * FROM favored_meals WHERE favoringUserEmail = :userEmail")
    suspend fun getFavoredMeals(userEmail: String): List<FavoredMeal>

    @Query("SELECT * FROM favored_meals WHERE mealId = :mealId AND favoringUserEmail = :userEmail")
    suspend fun isMealFavored(mealId: String, userEmail: String): FavoredMeal?

    @Query("DELETE FROM favored_meals WHERE mealId = :mealId AND favoringUserEmail = :userEmail")
    suspend fun removeMealFromFavorites(mealId: String, userEmail: String)

    @Query("INSERT INTO favored_meals (mealId, favoringUserEmail) VALUES (:mealId, :userEmail)")
    suspend fun addMealToFavorites(mealId: String, userEmail: String)
}