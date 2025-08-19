package com.example.recipetreasures.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipetreasures.data.model.FavoredMeal
import com.example.recipetreasures.data.model.Meals
import com.example.recipetreasures.data.repo.FavoredMealRepository
import com.example.recipetreasures.data.repo.MealsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavoredMealViewModel(
    private val dbRepo: FavoredMealRepository,
    private val networkRepo: MealsRepository? = null,
    private val userEmail: String
) : ViewModel() {

    private val _favoredMeals = MutableLiveData<List<Meals>>()
    val favoredMeals: LiveData<List<Meals>> get() = _favoredMeals

    init {
        fetchFavoredMeals()
    }

    fun fetchFavoredMeals() {
        if (networkRepo == null)
            return
        viewModelScope.launch {
            val favoredMealIds = dbRepo.getFavoredMeals(userEmail)
            val mealsList = mutableListOf<Meals>()
            for (favoredMeal in favoredMealIds) {
                val meal = networkRepo.getMealById(favoredMeal.mealId)
                mealsList.add(meal)
            }
            _favoredMeals.postValue(mealsList)
        }
    }
    suspend fun isMealFavored(mealId: String): Boolean {
        return dbRepo.isMealFavored(mealId, userEmail)

    }

    suspend fun addMealToFavorites(mealId: String)  {
        dbRepo.addMealToFavorites(mealId, userEmail)
        fetchFavoredMeals()
    }

    suspend fun removeMealFromFavorites(mealId: String)  {
        dbRepo.removeMealFromFavorites(mealId, userEmail)
        fetchFavoredMeals()
    }

}
/*
class FavoredMealViewModel(val dbRepo: FavoredMealRepository, val NetworkRepo: MealsRepository, val userEmail: String) : ViewModel() {
    private val favoredMeals: LiveData<List<FavoredMeal>>
        get() = dbRepo.getFavoredMeals(userEmail)

    suspend fun isMealFavored(mealId: String): Boolean {
        return dbRepo.isMealFavored(mealId, userEmail)
    }

    suspend fun addMealToFavorites(mealId: String) =
        dbRepo.addMealToFavorites(mealId, userEmail)

    suspend fun removeMealFromFavorites(mealId: String) =
        dbRepo.removeMealFromFavorites(mealId, userEmail)
}*/
