package com.example.recipetreasures.data.model

import com.google.gson.annotations.SerializedName

data class SearchRecipe(
    @SerializedName("meals" )
    var meals : List<Meals> = emptyList<Meals>()
)