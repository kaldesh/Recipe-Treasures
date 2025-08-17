package com.example.recipetreasures.data

import com.google.gson.annotations.SerializedName


data class SearchRecipe(
    @SerializedName("meals" )
    var meals : List<Meals>? = null
)
