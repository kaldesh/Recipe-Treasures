package com.example.recipetreasures.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipetreasures.data.repo.FavoredMealRepository
import com.example.recipetreasures.data.repo.MealsRepository

class FavoredMealViewModelFactory(private val dbRepo: FavoredMealRepository, private val networkRepo: MealsRepository? = null, private val userEmail: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoredMealViewModel::class.java)) {
            return FavoredMealViewModel(
                dbRepo,
                networkRepo,
                userEmail
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}