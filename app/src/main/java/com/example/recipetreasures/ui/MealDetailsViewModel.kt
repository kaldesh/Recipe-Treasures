package com.example.recipetreasures.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipetreasures.data.repo.MealsRepository
import kotlinx.coroutines.launch

class MealDetailsViewModel(
    private val repository: MealsRepository,
) : ViewModel() {

    private val _meal = MutableLiveData<MealsUiModel>()
    val mealDetails: LiveData<MealsUiModel> get() = _meal

    fun getMealById(mealId: String) {
        viewModelScope.launch {
            val meal = repository.getMealById(mealId)
            _meal.postValue(meal.toUiModel(false))
        }
    }
}
