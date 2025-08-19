package com.example.recipetreasures.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "favored_meals",
    primaryKeys = ["mealId", "favoringUserEmail"]
)
data class FavoredMeal(
    val mealId: String,
    val favoringUserEmail: String
)
