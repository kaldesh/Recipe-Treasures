package com.example.recipetreasures.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipetreasures.ui.MealsRepository

class MealDetailsViewModelFactory (private val repo: MealsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealDetailsViewModel::class.java)) {
            return MealDetailsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}