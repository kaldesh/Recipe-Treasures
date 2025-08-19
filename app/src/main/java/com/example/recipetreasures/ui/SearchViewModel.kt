package com.example.recipetreasures.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipetreasures.data.model.Meals
import com.example.recipetreasures.data.repo.MealsRepository
import kotlinx.coroutines.launch
class SearchViewModel(private val repository: MealsRepository) : ViewModel() {

    private val _results = MutableLiveData<List<Meals>>()
    val results: LiveData<List<Meals>> = _results

    fun searchMeals(query: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchMeals(query)
                _results.postValue(response.meals ?: emptyList())
            } catch (e: Exception) {
                _results.postValue(emptyList())
            }
        }
    }
}
