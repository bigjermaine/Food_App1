package com.example.food_app1.Presentation.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_app1.data.local.Respository.FavoritesRepository
import com.example.food_app1.Presentation.Models.MealDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val meals: List<MealDetail>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class FavoritesViewModel(
    private val repository: FavoritesRepository
) : ViewModel() {

    val favorites = repository.favorites()
        .stateIn(
            viewModelScope,
            SharingStarted.Companion.WhileSubscribed(5_000),
            emptyList()
        )
    fun isFavorite(mealId: String): StateFlow<Boolean> =
        repository.isFavorite(mealId)
            .stateIn(
                viewModelScope,
                SharingStarted.Companion.WhileSubscribed(5_000),
                false
            )

    fun delete(meal: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFavorite(meal)
        }
    }
    fun toggleFavorite(meal: MealDetail, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.toggleFavorite(meal, isFavorite)
        }
    }
}