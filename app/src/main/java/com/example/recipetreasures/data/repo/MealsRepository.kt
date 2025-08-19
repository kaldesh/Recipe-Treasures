package com.example.recipetreasures.data.repo

import com.example.recipetreasures.data.model.Meals
import com.example.recipetreasures.data.model.SearchRecipe
import com.example.recipetreasures.data.retrofit.RecipeEndPoint

class MealsRepository(private val api: RecipeEndPoint) {
    suspend fun getMealsByLetter(letter: String): SearchRecipe {
        return api.getMealsByLetter(letter)
    }

    suspend fun searchMeals(query: String): SearchRecipe {
        return api.searchMeals(query)
    }

    suspend fun getMealById(id: String): Meals {
        return api.getMealById(id).meals.get(0)
    }
}