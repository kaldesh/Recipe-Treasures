package com.example.recipetreasures.data.retrofit

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AllAPi {
    const val BASE_URL="https://www.themealdb.com/api/json/v1/1/"
    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()

    val recipeEndPoint =retrofit.create(RecipeEndPoint::class.java)
}