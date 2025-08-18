package com.example.recipetreasures.ui

import com.example.recipetreasures.data.Meals
import com.example.recipetreasures.data.RecipeEndPoint
import com.example.recipetreasures.data.SearchRecipe

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
