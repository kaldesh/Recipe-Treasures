package com.example.recipetreasures.ui

import com.example.recipetreasures.data.model.Meals


data class MealsUiModel(
    val id: String,
    val name: String,
    val category: String,
    val area: String,
    val instructions: String,
    val thumbnailUrl: String,
    val tags: String?,
    val youtubeUrl: String,
    val ingredients: List<String>,
    val measures: List<String>
)
fun Meals.toUiModel(): MealsUiModel {
    val ingredientsList = listOfNotNull(
        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15,
        strIngredient16, strIngredient17, strIngredient18, strIngredient19, strIngredient20
    ).filter { it.isNotBlank() }

    val measuresList = listOfNotNull(
        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15,
        strMeasure16, strMeasure17, strMeasure18, strMeasure19, strMeasure20
    ).filter { it.isNotBlank() }

    return MealsUiModel(
        id = idMeal ?: "",
        name = strMeal ?: "",
        category = strCategory ?: "",
        area = strArea ?: "",
        instructions = strInstructions ?: "",
        thumbnailUrl = strMealThumb ?: "",
        tags = strTags,
        youtubeUrl = strYoutube ?: "",
        ingredients = ingredientsList,
        measures = measuresList
    )
}
