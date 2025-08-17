package com.example.recipetreasures.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.recipetreasures.data.Meals
import kotlinx.coroutines.launch

class MealsViewModel(private val repository: MealsRepository) : ViewModel() {

    private val _meals = MutableLiveData<List<Meals>>()
    val meals: LiveData<List<Meals>> get() = _meals

    fun fetchAllMeals() {
        viewModelScope.launch {
            val letters = ('a'..'z')
            val allMeals = mutableListOf<Meals>()

            for (letter in letters) {
                try {
                    val response = repository.getMealsByLetter(letter.toString())
                    val mealsResponse = response.meals // store in a local immutable variable

                    if (mealsResponse.isNullOrEmpty()) {
                        Log.d("MealsViewModel", "No meals for letter: $letter")
                    } else {
                        Log.d("MealsViewModel", "Found ${mealsResponse.size} meals for letter: $letter")
                        allMeals.addAll(mealsResponse)
                        _meals.postValue(allMeals.toList()) // post incremental updates
                    }
                } catch (e: Exception) {
                    Log.e("MealsViewModel", "Error fetching meals for letter $letter", e)
                }
            }
        }
    }
    fun searchMeals(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchMeals(query)
                _meals.postValue(response.meals ?: emptyList())
            } catch (e: Exception) {
                _meals.postValue(emptyList())
            }
        }
    }

}
