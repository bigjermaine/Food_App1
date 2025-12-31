package com.example.food_app1.Presentation.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app1.data.remote.Data.MealRespository
import com.example.food_app1.Presentation.Models.MealDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
class HomeViewModel( val mealRespository: MealRespository): ViewModel(){
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    private val _meals = MutableStateFlow<List<MealDetail>>(emptyList())
    val meals = _meals.asStateFlow()
    val uiState = _uiState.asStateFlow()
    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val meals = mealRespository.getMeals()
                _meals.value = meals.meals
                Log.d("HomeViewModel", "Meals response: ${meals.meals}")
                _uiState.value = HomeUiState.Success(meals.meals)

            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Failed to load meals")
            }
        }
        didSearch()
    }

    fun didSearch() {
        viewModelScope.launch {
            _query
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collectLatest { query ->
                    try {
                        val response = mealRespository.searchMeal(query)
                        _meals.value = response.meals.orEmpty()
                        Log.d("HomeViewModel", "Meals response: $response.meals}")
                        _uiState.value = HomeUiState.Success(response.meals)
                    } catch (e: Exception) {
                        _meals.value = emptyList()
                    }
                }
        }
    }

     fun MealDetail.ingredientsUpTo10(): List<String> =
        listOfNotNull(
            strIngredient1,
            strIngredient2,
            strIngredient3,
            strIngredient4,
            strIngredient5,
            strIngredient6,
            strIngredient7,
            strIngredient8,
            strIngredient9,
            strIngredient10
        ).filter { it.isNotBlank() }

   suspend fun getMeals() {
       _meals.value = mealRespository.getMeals().meals

    }

    fun searchMeal(search: String) {
        if (search.isBlank()) return

        viewModelScope.launch {
            try {
                val response = mealRespository.searchMeal(search)
                _meals.value = response.meals.orEmpty()
            } catch (e: Exception) {
                _meals.value = emptyList()
            }
        }
    }
    fun onSearchQueryChanged(query: String) {
        _query.value = query
    }
}