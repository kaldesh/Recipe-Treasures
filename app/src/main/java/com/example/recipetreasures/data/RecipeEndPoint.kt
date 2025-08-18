package com.example.recipetreasures.data

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeEndPoint {
    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): SearchRecipe

    @GET("search.php")
    suspend fun getMealsByLetter(@Query("f") letter: String): SearchRecipe

    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): SearchRecipe
}

