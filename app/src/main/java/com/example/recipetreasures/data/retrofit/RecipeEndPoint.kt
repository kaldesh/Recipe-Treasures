package com.example.recipetreasures.data.retrofit

import com.example.recipetreasures.data.model.SearchRecipe
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

