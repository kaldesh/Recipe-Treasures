package com.example.recipetreasures.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MealsViewModelFactory(private val repo: MealsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealsViewModel::class.java)) {
            return MealsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}